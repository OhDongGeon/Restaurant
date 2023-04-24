package com.example.restaurant.domain.entity;

import com.example.restaurant.domain.type.ReservationType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reservation extends BaseEntity {
    @Id
    @Column(columnDefinition = "VARCHAR(20) NOT NULL")
    private String reservationId;
    @Column(columnDefinition = "VARCHAR(10) NOT NULL")
    private String memberName;
    @Column(columnDefinition = "VARCHAR(11) NOT NULL")
    private String memberPhone;
    private LocalDateTime reservationDate;
    private Long reservationPeople;
    @Column(columnDefinition = "VARCHAR(10) NOT NULL")
    @Enumerated(EnumType.STRING)
    private ReservationType reservationApproval;
    private boolean reservationVisit;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "restaurantId")
    private Restaurant restaurant;
}
