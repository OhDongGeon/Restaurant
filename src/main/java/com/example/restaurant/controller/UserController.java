package com.example.restaurant.controller;

import com.example.restaurant.Service.ManagerService;
import com.example.restaurant.Service.UserService;
import com.example.restaurant.domain.dto.MemberDto;
import com.example.restaurant.domain.dto.ReservationDto;
import com.example.restaurant.domain.entity.Member;
import com.example.restaurant.domain.entity.Restaurant;
import com.example.restaurant.domain.vo.MemberVo;
import com.example.restaurant.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ManagerService managerService;
    private final TokenProvider tokenProvider;
    private final String TOKEN_HEADER = "MEMBER_TOKEN";


    // 회원 정보 조회
    @GetMapping("/info")
    public ResponseEntity<MemberDto.MemberInfo> memberInfo(@RequestHeader(name = TOKEN_HEADER) String token) {
        MemberVo memberVo = tokenProvider.getMemberVo(token);
        Member member = userService.chekMember(memberVo.getMemberId());

        return ResponseEntity.ok(MemberDto.MemberInfo.search(member));
    }


    // 파트너쉽 가입신청
    @PutMapping("partnership")
    public ResponseEntity<String> applyPartnerShip(@RequestHeader(name = TOKEN_HEADER) String token) {
        MemberVo memberVo = tokenProvider.getMemberVo(token);
        Member member = userService.chekMember(memberVo.getMemberId());

        return ResponseEntity.ok(userService.applyPartnerShip(member));
    }


    // 회원 예약 내역 조회
    @GetMapping("/search/reservation")
    public ResponseEntity<List<ReservationDto.SearchMemberReservation>> searchMemberReservation(@RequestHeader(name = TOKEN_HEADER) String token) {
        MemberVo memberVo = tokenProvider.getMemberVo(token);
        Member member = userService.chekMember(memberVo.getMemberId());

        return ResponseEntity.ok(userService.searchMemberReservation(member.getMemberId()));
    }


    // 회원 식당 예약
    @PostMapping("/reservation/{restaurant}")
    public ResponseEntity<ReservationDto.SearchReservation> addReservation(@RequestHeader(name = TOKEN_HEADER) String token,
                                                                           @PathVariable("restaurant") Long restaurantId,
                                                                           @RequestBody ReservationDto.AddReservation addReservation) {
        MemberVo memberVo = tokenProvider.getMemberVo(token);
        Member member = userService.chekMember(memberVo.getMemberId());
        Restaurant restaurant = managerService.checkRestaurant(restaurantId);

        return ResponseEntity.ok(ReservationDto.SearchReservation.search(userService.addReservation(member, restaurant, addReservation)));
    }


    // 예약 취소
    @DeleteMapping("/reservation/{reservationid}")
    public ResponseEntity<String> cancelReservation (@RequestHeader(name = TOKEN_HEADER) String token,
                                                     @PathVariable("reservationid") String reservationId) {
        MemberVo memberVo = tokenProvider.getMemberVo(token);
        userService.chekMember(memberVo.getMemberId());

        return ResponseEntity.ok(userService.cancelReservation(reservationId));
    }
}
