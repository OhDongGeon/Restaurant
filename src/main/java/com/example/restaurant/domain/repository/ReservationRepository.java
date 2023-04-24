package com.example.restaurant.domain.repository;

import com.example.restaurant.domain.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByReservationId(String reservationId);

    // 예약 정보 확인
    @Query(value = "SELECT R " +
                     "FROM Reservation R " +
                    "WHERE R.reservationId              = :reservationId " +
                      "AND R.restaurant.restaurantId    = :restaurantId ")
    Optional<Reservation> checkReservation(String reservationId, Long restaurantId);

    // 예약번호로 사용하기 위해 가장큰 데이터 조회
    @Query(value = "SELECT MAX(R.reservationId) FROM Reservation R WHERE R.reservationId LIKE :reservationId%")
    Optional<String> findReservationMaxId(String reservationId);

    // 예약자가 맞는지 확인
    Optional<Reservation> findByReservationIdAndMemberName(String reservationId, String memberName);

    // 회원 예약 내역 조회
    List<Reservation> findByMember_MemberIdOrderByReservationDateDesc(Long memberId);

    // 식당기준 해당 아이디 예약이 59분 간격으로 데이터가 있는지 확인
    boolean existsByMember_MemberIdAndRestaurant_RestaurantIdAndReservationDateBetween(
            Long memberId, Long restaurantId, LocalDateTime beforeDate, LocalDateTime afterDate);

    // 시간별 예약 승인된 인원수
    @Query(value = "SELECT (CASE WHEN SUM(RV.reservationPeople) + :reservationPeople <= RT.restaurantTable " +
                                "THEN true ELSE false END) " +
                     "FROM Restaurant RT " +
               "INNER JOIN Reservation RV " +
                       "ON RV.restaurant.restaurantId   = RT.restaurantId " +
                    "WHERE RT.restaurantId              = :restaurantId " +
                      "AND RV.reservationDate           = :reservationDate " +
                      "AND RV.reservationApproval       = 'approval' " +
                 "GROUP BY RT.restaurantId" +
                        ", RV.reservationDate")
    Optional<Boolean> countReservationsByTime(Long restaurantId, LocalDateTime reservationDate, Long reservationPeople);
}
