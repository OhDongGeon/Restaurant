package com.example.restaurant.controller;

import com.example.restaurant.Service.PartnerService;
import com.example.restaurant.domain.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class PartnerController {
    private final PartnerService partnerService;


    // 예약 승인
    @PutMapping("/partner/approval")
    public ResponseEntity<List<MemberDto.MemberInfo>> approvalPartnerShip(@RequestBody List<MemberDto.MemberId> memberIdList) {
        return ResponseEntity.ok(partnerService.approvalPartnerShip(memberIdList));
    }
}
