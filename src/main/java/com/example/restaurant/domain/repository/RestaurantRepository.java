package com.example.restaurant.domain.repository;

import com.example.restaurant.domain.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findByRestaurantId(Long restaurantId);

    boolean existsByRestaurantName(String restaurantName);

    List<Restaurant> findByMember_MemberIdAndRestaurantNameStartsWith(Long memberId, String restaurantName);

    List<Restaurant> findByRestaurantNameStartsWith(String restaurantName);
}
