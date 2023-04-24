package com.example.restaurant.Service;

import com.example.restaurant.domain.dto.ReservationDto;
import com.example.restaurant.domain.dto.RestaurantDto;
import com.example.restaurant.domain.entity.Member;
import com.example.restaurant.domain.entity.Reservation;
import com.example.restaurant.domain.entity.Restaurant;
import com.example.restaurant.domain.repository.ReservationRepository;
import com.example.restaurant.domain.repository.RestaurantRepository;
import com.example.restaurant.exception.FindException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.restaurant.domain.type.ReservationType.APPROVAL;
import static com.example.restaurant.domain.type.ReservationType.REFUSE;
import static com.example.restaurant.exception.ErrorCode.*;


@Service
@AllArgsConstructor
public class ManagerService {
    private final RestaurantRepository restaurantRepository;
    private final ReservationRepository reservationRepository;


    // 식당 체크
    public Restaurant checkRestaurant(Long restaurantId) {
        return restaurantRepository.findByRestaurantId(restaurantId).orElseThrow(() -> new FindException(NOT_FIND_RESTAURANT_ID));
    }


    // 식당 조회 (로그인 아닌 상태에서도 조회 가능)
    @Transactional
    public List<RestaurantDto.SearchRestaurant> searchRestaurant(String restaurantName) {
        List<Restaurant> restaurantList = restaurantRepository.findByRestaurantNameStartsWith(restaurantName);
        return restaurantList.stream().map(RestaurantDto.SearchRestaurant::search).collect(Collectors.toList());
    }


    // 매니저 기준 등록된 매장 조회
    @Transactional
    public List<RestaurantDto.SearchRestaurant> searchRestaurant(Long memberId, String restaurantName) {
        List<Restaurant> restaurantList = restaurantRepository.findByMember_MemberIdAndRestaurantNameStartsWith(memberId, restaurantName);
        return restaurantList.stream().map(RestaurantDto.SearchRestaurant::search).collect(Collectors.toList());
    }


    // 매니저 기준 식당의 예약 확인
    @Transactional
    public List<Restaurant> searchReservation(Long memberId, String restaurantName) {
        return restaurantRepository.findByMember_MemberIdAndRestaurantNameStartsWith(memberId, restaurantName);
    }


    // 식당 저장
    @Transactional
    public Restaurant addRestaurant(Member member, RestaurantDto.AddRestaurant addRestaurant) {
        if (restaurantRepository.existsByRestaurantName(addRestaurant.getRestaurantName())) {
            throw new FindException(OVERLAP_RESTAURANT_NAME);
        }
        Restaurant restaurant = RestaurantDto.AddRestaurant.save(addRestaurant);
        member.getRestaurants().add(restaurant);
        return restaurant;
    }


    // 예약 승인
    @Transactional
    public List<ReservationDto.SearchReservation> approvalReservation(Long restaurantId, List<ReservationDto.HandleReservation> approvalReservation) {
        List<Reservation> reservationList = new ArrayList<>();
        for (ReservationDto.HandleReservation item : approvalReservation) {
            Reservation reservation = reservationRepository.checkReservation(item.getReservationId(), restaurantId)
                    .orElseThrow(() -> new FindException(NOT_MATCH_RESTAURANT_ID));

            // 과거 예약은 승인 처리할 수 없다.
            if (reservation.getReservationDate().isBefore(LocalDateTime.now())) {
                throw new FindException(BEFORE_APPROVAL_RESERVATION_DATE);
            }


            if (!reservation.getReservationApproval().equals(APPROVAL)) {
                // 시간별 예약 승인된 예약자의 수를 식당 자리의 수와 비교
                Boolean checkEmptySeats = reservationRepository.countReservationsByTime(
                        restaurantId, reservation.getReservationDate(), reservation.getReservationPeople()).orElse(true);

                if (!checkEmptySeats) {
                    throw new FindException(CHECK_EMPTY_SEATS);
                }
            }

            reservation.setReservationApproval(APPROVAL);
            reservationList.add(reservation);
        }
        return reservationList.stream().map(ReservationDto.SearchReservation::search).collect(Collectors.toList());
    }


    // 예약 거절
    @Transactional
    public List<ReservationDto.SearchReservation> refuseReservation(Long restaurantId, List<ReservationDto.HandleReservation> approvalReservation) {
        List<Reservation> reservationList = new ArrayList<>();
        for (ReservationDto.HandleReservation item : approvalReservation) {
            Reservation reservation = reservationRepository.checkReservation(item.getReservationId(), restaurantId)
                    .orElseThrow(() -> new FindException(NOT_MATCH_RESTAURANT_ID));

            if (reservation.getReservationDate().isBefore(LocalDateTime.now())) {
                throw new FindException(BEFORE_REFUSE_RESERVATION_DATE);
            }

            reservation.setReservationApproval(REFUSE);
            reservationList.add(reservation);
        }
        return reservationList.stream().map(ReservationDto.SearchReservation::search).collect(Collectors.toList());
    }
}
