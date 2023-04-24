package com.example.restaurant.domain.entity;

import com.example.restaurant.domain.type.UserType;
import lombok.*;
import org.hibernate.envers.AuditOverride;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class Member extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;


    @Column(unique = true, columnDefinition = "VARCHAR(50) NOT NULL")
    private String memberEmail;
    private String memberPassword;

    @Column(columnDefinition = "VARCHAR(10) NOT NULL")
    private String memberName;
    @Column(columnDefinition = "VARCHAR(20) NOT NULL")
    private String memberNickName;
    @Column(columnDefinition = "VARCHAR(10) NOT NULL")
    private String memberBirthday;
    @Column(columnDefinition = "VARCHAR(11) NOT NULL")
    private String memberPhone;
    @Column(columnDefinition = "VARCHAR(10) NOT NULL")
    @Enumerated(EnumType.STRING)
    private UserType memberFlag; // MANAGER:매니저(식당), USER:사용자(예약)


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "memberId")
    private List<Restaurant> restaurants = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "memberId")
    private List<Reservation> reservations = new ArrayList<>();
}
