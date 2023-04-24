package com.example.restaurant.controller;

import com.example.restaurant.Service.CommonService;
import com.example.restaurant.Service.ManagerService;
import com.example.restaurant.domain.dto.MemberDto;
import com.example.restaurant.domain.dto.RestaurantDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/common")
@RequiredArgsConstructor
public class CommonController {
    private final CommonService commonService;
    private final ManagerService managerService;


    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> memberSignUp(@RequestBody MemberDto.SignUp signUpDto) {
        return ResponseEntity.ok(commonService.memberSignUp(signUpDto));
    }


    // 로그인
    @PostMapping("/signin")
    public ResponseEntity<String> memberSignIn(@RequestBody MemberDto.SignIn signInDto) {
        return ResponseEntity.ok(commonService.memberSingIn(signInDto));
    }


    // 식당 조회 (로그인 아닌 상태에서도 조회 가능)
    @GetMapping("/search")
    public ResponseEntity<List<RestaurantDto.SearchRestaurant>> searchRestaurant(@RequestParam String restaurantName) {
        return ResponseEntity.ok(managerService.searchRestaurant(restaurantName));
    }
}
