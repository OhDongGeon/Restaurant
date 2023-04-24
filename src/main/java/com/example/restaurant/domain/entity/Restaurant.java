package com.example.restaurant.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restaurantId;

    @Column(columnDefinition = "VARCHAR(30) NOT NULL")
    private String restaurantName;
    @Column(columnDefinition = "VARCHAR(15) NOT NULL")
    private String restaurantPhone;
    @Column(columnDefinition = "VARCHAR(150) NOT NULL")
    private String restaurantAddress;
    private Long restaurantTable;
    private String restaurantMemo;


    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "restaurantId")
    private List<Reservation> reservations = new ArrayList<>();
}
