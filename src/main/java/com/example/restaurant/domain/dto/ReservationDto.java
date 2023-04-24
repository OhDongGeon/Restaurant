package com.example.restaurant.domain.dto;

import com.example.restaurant.domain.entity.Reservation;
import com.example.restaurant.domain.entity.Restaurant;
import com.example.restaurant.domain.type.ReservationType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.restaurant.domain.type.ReservationType.WAIT;


public class ReservationDto {
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchReservation {
        private String reservationId;
        private String reservationNumber;
        private String memberName;
        private String memberPhone;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        private LocalDateTime reservationDate;
        private Long reservationPeople;
        private ReservationType reservationApproval;

        public static ReservationDto.SearchReservation search(Reservation reservation) {
            String reservationId = reservation.getReservationId();
            return SearchReservation.builder()
                    .reservationId(reservationId)
                    .reservationNumber(reservationId.substring(reservationId.length() - 4))
                    .memberName(reservation.getMemberName())
                    .memberPhone(reservation.getMemberPhone())
                    .reservationDate(reservation.getReservationDate())
                    .reservationPeople(reservation.getReservationPeople())
                    .reservationApproval(reservation.getReservationApproval())
                    .build();
        }
    }


    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchMemberReservation {
        private String restaurantName;
        private String reservationNumber;
        private String memberName;
        private String memberPhone;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        private LocalDateTime reservationDate;
        private Long reservationPeople;
        private ReservationType reservationApproval;

        public static ReservationDto.SearchMemberReservation search(Reservation reservation) {
            String reservationId = reservation.getReservationId();
            return SearchMemberReservation.builder()
                    .restaurantName(reservation.getRestaurant().getRestaurantName())
                    .reservationNumber(reservationId.substring(reservationId.length() - 4))
                    .memberName(reservation.getMemberName())
                    .memberPhone(reservation.getMemberPhone())
                    .reservationDate(reservation.getReservationDate())
                    .reservationPeople(reservation.getReservationPeople())
                    .reservationApproval(reservation.getReservationApproval())
                    .build();
        }
    }


    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchReservationList {
        private String restaurantName;
        private String restaurantPhone;
        private String restaurantAddress;
        private List<SearchReservation> reservationList;

        public static SearchReservationList search(Restaurant restaurant) {
            List<ReservationDto.SearchReservation> list = restaurant.getReservations()
                    .stream().map(ReservationDto.SearchReservation::search).collect(Collectors.toList());

            return SearchReservationList.builder()
                    .restaurantName(restaurant.getRestaurantName())
                    .restaurantPhone(restaurant.getRestaurantPhone())
                    .restaurantAddress(restaurant.getRestaurantAddress())
                    .reservationList(list)
                    .build();
        }
    }


    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddReservation {
        private String reservationId;
        private String memberName;
        private String memberPhone;
        private LocalDateTime reservationDate;
        private Long reservationPeople;
        private String reservationApproval;
        private boolean reservationVisit;

        public static Reservation save(AddReservation addReservation) {
            return Reservation.builder()
                    .reservationId(addReservation.getReservationId())
                    .memberName(addReservation.getMemberName())
                    .memberPhone(addReservation.getMemberPhone())
                    .reservationDate(addReservation.getReservationDate())
                    .reservationPeople(addReservation.getReservationPeople())
                    .reservationApproval(WAIT)
                    .reservationVisit(false)
                    .build();
        }
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HandleReservation {
        private String reservationId;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VisitReservation {
        private String reservationNumber;
        private String memberName;
    }
}
