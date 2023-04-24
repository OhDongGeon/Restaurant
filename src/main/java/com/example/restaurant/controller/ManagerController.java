package com.example.restaurant.controller;

import com.example.restaurant.Service.ManagerService;
import com.example.restaurant.Service.UserService;
import com.example.restaurant.domain.dto.ReservationDto;
import com.example.restaurant.domain.dto.RestaurantDto;
import com.example.restaurant.domain.entity.Member;
import com.example.restaurant.domain.vo.MemberVo;
import com.example.restaurant.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {
    private final UserService userService;
    private final ManagerService managerService;
    private final TokenProvider tokenProvider;
    public final String TOKEN_HEADER = "MEMBER_TOKEN";


    // 매니저 기준 등록된 식당 조회
    @GetMapping("/search/restaurant") //
    public ResponseEntity<RestaurantDto.SearchRestaurantList> searchRestaurant(@RequestHeader(name = TOKEN_HEADER) String token,
                                                                               @RequestParam String restaurantName) {
        MemberVo memberVo = tokenProvider.getMemberVo(token);
        Member member = userService.chekMember(memberVo.getMemberId());
        userService.checkManager(member.getMemberFlag());

        return ResponseEntity.ok(RestaurantDto.SearchRestaurantList.search(member, managerService.searchRestaurant(member.getMemberId(), restaurantName)));
    }


    // 매니저 기준 식당의 예약 확인
    @GetMapping("/search/reservation")
    public ResponseEntity<List<ReservationDto.SearchReservationList>> searchReservation(@RequestHeader(name = TOKEN_HEADER) String token,
                                                                                        @RequestParam String restaurantName) {
        MemberVo memberVo = tokenProvider.getMemberVo(token);
        Member member = userService.chekMember(memberVo.getMemberId());
        userService.checkManager(member.getMemberFlag());

        return ResponseEntity.ok(managerService.searchReservation(member.getMemberId(), restaurantName)
                .stream().map(ReservationDto.SearchReservationList::search).collect(Collectors.toList()));
    }


    // 식당 저장 후 조회
    @PostMapping("/add/restaurant")
    public ResponseEntity<RestaurantDto.SearchRestaurant> addRestaurant(@RequestHeader(name = TOKEN_HEADER) String token,
                                                                        @RequestBody RestaurantDto.AddRestaurant addRestaurant) {
        MemberVo memberVo = tokenProvider.getMemberVo(token);
        Member member = userService.chekMember(memberVo.getMemberId());
        userService.checkManager(member.getMemberFlag());

        return ResponseEntity.ok(RestaurantDto.SearchRestaurant.search(managerService.addRestaurant(member, addRestaurant)));
    }


    // 예약 승인
    @PutMapping("/approval/reservation/{restaurantId}")
    public ResponseEntity<List<ReservationDto.SearchReservation>> approvalReservation(@RequestHeader(name = TOKEN_HEADER) String token,
                                                                                      @PathVariable("restaurantId") Long restaurantId,
                                                                                      @RequestBody List<ReservationDto.HandleReservation> approvalReservation) {
        MemberVo memberVo = tokenProvider.getMemberVo(token);
        Member member = userService.chekMember(memberVo.getMemberId());
        userService.checkManager(member.getMemberFlag());

        return ResponseEntity.ok(managerService.approvalReservation(restaurantId, approvalReservation));
    }


    // 예약 거절
    @PutMapping("/refuse/reservation/{restaurantId}")
    public ResponseEntity<List<ReservationDto.SearchReservation>> refuseReservation(@RequestHeader(name = TOKEN_HEADER) String token,
                                                                                    @PathVariable("restaurantId") Long restaurantId,
                                                                                    @RequestBody List<ReservationDto.HandleReservation> approvalReservation) {
        MemberVo memberVo = tokenProvider.getMemberVo(token);
        Member member = userService.chekMember(memberVo.getMemberId());
        userService.checkManager(member.getMemberFlag());

        return ResponseEntity.ok(managerService.refuseReservation(restaurantId, approvalReservation));
    }
}
