package com.example.restaurant.controller;

import com.example.restaurant.Service.KioskService;
import com.example.restaurant.domain.dto.ReservationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/kiosk")
@RequiredArgsConstructor
public class KioskController {
    private final KioskService kioskService;


    // 예약자 식당 방문
    @PutMapping("/visit/{restaurantId}")
    public ResponseEntity<String> visitRestaurant(@PathVariable("restaurantId") Long restaurantId,
                                                  @RequestBody ReservationDto.VisitReservation visitReservation) {
        return ResponseEntity.ok(kioskService.visitRestaurant(restaurantId, visitReservation));
    }
}
