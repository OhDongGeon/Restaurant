package com.example.restaurant.Service;

import com.example.restaurant.domain.dto.ReservationDto;
import com.example.restaurant.domain.entity.Reservation;
import com.example.restaurant.domain.repository.ReservationRepository;
import com.example.restaurant.domain.type.ReservationType;
import com.example.restaurant.exception.FindException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.example.restaurant.domain.type.ReservationType.APPROVAL;
import static com.example.restaurant.exception.ErrorCode.NOT_FIND_RESERVATION;


@Service
@AllArgsConstructor
public class KioskService {
    private final ReservationRepository reservationRepository;


    // 예약자 식당 방문
    @Transactional
    public String visitRestaurant(Long restaurantId, ReservationDto.VisitReservation visitReservation) {
        String reservationId = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + restaurantId + visitReservation.getReservationNumber();

        Reservation reservation = reservationRepository.findByReservationIdAndMemberName(
                        reservationId, visitReservation.getMemberName())
                .orElseThrow(() -> new FindException(NOT_FIND_RESERVATION));

        ReservationType reservationApproval = reservation.getReservationApproval();

        // 예약 승인 되지 않은 내역 체크
        if (!reservationApproval.equals(APPROVAL)) {
            throw new FindException(NOT_FIND_RESERVATION);
        }

        LocalDateTime reservationDate = reservation.getReservationDate();
        LocalDateTime now = LocalDateTime.now();
        String message;

        // 예약시간 10분 전에 방문, 예약 시간 전까지 오면 카운터 문의, 취소
        if (now.isBefore(reservationDate.minusMinutes(10))) {
            reservation.setReservationVisit(true);
            message = reservation.getMemberName() + " 고객님 환영합니다.";
        } else if (now.isBefore(reservationDate)) {
            message = "예약 10분 전 방문 하셔야합니다. 카운터에 문의 하세요.";
        } else {
            message = "취소된 예약 입니다.";
        }

        return message;
    }
}
