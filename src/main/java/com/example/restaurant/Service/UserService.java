package com.example.restaurant.Service;


import com.example.restaurant.domain.dto.ReservationDto;
import com.example.restaurant.domain.entity.Member;
import com.example.restaurant.domain.entity.Reservation;
import com.example.restaurant.domain.entity.Restaurant;
import com.example.restaurant.domain.repository.MemberRepository;
import com.example.restaurant.domain.repository.ReservationRepository;
import com.example.restaurant.domain.type.UserType;
import com.example.restaurant.exception.FindException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.restaurant.domain.type.UserType.APPLY;
import static com.example.restaurant.domain.type.UserType.MANAGER;
import static com.example.restaurant.exception.ErrorCode.*;

@Service
@AllArgsConstructor
public class UserService {
    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;


    // 회원 조회
    public Member chekMember(Long memberId) {
        return memberRepository.findByMemberId(memberId).orElseThrow(() -> new FindException(NOT_FIND_MEMBER_EMAIL));
    }


    // 매니저 체크
    public void checkManager(UserType memberFlag) {
        if (!memberFlag.equals(MANAGER)) {
            throw new FindException(NOT_MATCH_MEMBER_FLAG);
        }
    }


    // 파트너쉽 가입신청
    @Transactional
    public String applyPartnerShip(Member member) {
        member.setMemberFlag(APPLY);
        return "멤버쉽 가입을 신청 했습니다.";
    }


    // 회원 예약 내역 조회
    public List<ReservationDto.SearchMemberReservation> searchMemberReservation(Long memberId) {
        List<Reservation> reservationList = reservationRepository.findByMember_MemberIdOrderByReservationDateDesc(memberId);
        return reservationList.stream().map(ReservationDto.SearchMemberReservation::search).collect(Collectors.toList());
    }


    // 식당 예약
    @Transactional
    public Reservation addReservation(Member member, Restaurant restaurant, ReservationDto.AddReservation addReservation) {
        // 시간별 예약 승인된 예약자의 수를 식당 자리의 수와 비교
        Boolean checkEmptySeats = reservationRepository.countReservationsByTime(
                        restaurant.getRestaurantId(), addReservation.getReservationDate(), addReservation.getReservationPeople())
                .orElse(addReservation.getReservationPeople() <= restaurant.getRestaurantTable());

        if (!checkEmptySeats) {
            throw new FindException(CHECK_EMPTY_SEATS);
        }


        LocalDateTime reservationDate = addReservation.getReservationDate();
        LocalDateTime now = LocalDateTime.now();
        // 이전 날짜는 예약 불가 / 당일 예약 불가 / 식당별 예약이 1시간 이전, 이후에 잡혀 있을 경우 불가
        if (reservationDate.isBefore(now)) {
            throw new FindException(BEFORE_RESERVATION_DATE);
        } else if (reservationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                .equals(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))) {
            throw new FindException(TODAY_NOT_RESERVATION);
        } else if (reservationRepository.existsByMember_MemberIdAndRestaurant_RestaurantIdAndReservationDateBetween(
                member.getMemberId(), restaurant.getRestaurantId(), reservationDate.minusMinutes(59), reservationDate.plusMinutes(59))) {
            throw new FindException(FIND_ONE_HOUR_RESERVATION);
        }


        String reservationId;
        String reservationPartId = reservationDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + restaurant.getRestaurantId();
        String reservationMaxId = reservationRepository.findReservationMaxId(reservationPartId).orElse("");

        // 예약 번호 생성 (날짜 + 식당코드 + 순번)
        if (reservationMaxId.equals("")) {
            reservationId = reservationPartId + "0001";
        } else {
            reservationId = reservationPartId + String.format("%04d",
                    Integer.parseInt(reservationMaxId.replace(reservationPartId, "")) + 1);
        }
        addReservation.setReservationId(reservationId);

        Reservation reservation = ReservationDto.AddReservation.save(addReservation);
        member.getReservations().add(reservation);
        restaurant.getReservations().add(reservation);

        return reservation;
    }


    // 예약 취소
    @Transactional
    public String cancelReservation(String reservationId) {

        Reservation reservation = reservationRepository.findByReservationId(reservationId).orElseThrow(
                () -> new FindException(NOT_FIND_RESERVATION));

        ReservationDto.SearchReservation search = ReservationDto.SearchReservation.search(reservation);

        if (search.getReservationDate().isBefore(LocalDateTime.now())) {
            throw new FindException(BEFORE_CANCEL_RESERVATION_DATE);
        }

        reservationRepository.delete(reservation);

        return  search.getReservationDate().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분")) + " 예약을 취소 하였습니다.";
    }
}
