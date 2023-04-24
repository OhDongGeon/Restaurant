package com.example.restaurant.domain.dto;

import com.example.restaurant.domain.entity.Member;
import com.example.restaurant.domain.entity.Restaurant;
import lombok.*;

import java.util.List;


public class RestaurantDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchRestaurant {
        private String restaurantName;
        private String restaurantPhone;
        private String restaurantAddress;
        private Long restaurantTable;
        private String restaurantMemo;

        public static SearchRestaurant search(Restaurant restaurant) {
            return SearchRestaurant.builder()
                    .restaurantName(restaurant.getRestaurantName())
                    .restaurantPhone(restaurant.getRestaurantPhone())
                    .restaurantAddress(restaurant.getRestaurantAddress())
                    .restaurantTable(restaurant.getRestaurantTable())
                    .restaurantMemo(restaurant.getRestaurantMemo())
                    .build();
        }
    }


    @Getter
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchRestaurantList {
        private Long memberId;
        private String memberName;
        private String memberNickName;
        private List<SearchRestaurant> restaurantList;

        public static RestaurantDto.SearchRestaurantList search(Member member, List<RestaurantDto.SearchRestaurant> list) {
            return RestaurantDto.SearchRestaurantList.builder()
                    .memberId(member.getMemberId())
                    .memberName(member.getMemberName())
                    .memberNickName(member.getMemberNickName())
                    .restaurantList(list)
                    .build();
        }
    }


    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddRestaurant {
        private String restaurantName;
        private String restaurantPhone;
        private String restaurantAddress;
        private Long restaurantTable;
        private String restaurantMemo;

        public static Restaurant save(AddRestaurant addProductForm) {
            return Restaurant.builder()
                    .restaurantName(addProductForm.getRestaurantName())
                    .restaurantPhone(addProductForm.getRestaurantPhone())
                    .restaurantAddress(addProductForm.getRestaurantAddress())
                    .restaurantTable(addProductForm.getRestaurantTable())
                    .restaurantMemo(addProductForm.getRestaurantMemo())
                    .build();
        }
    }
}
