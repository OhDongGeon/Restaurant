package com.example.restaurant.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 회원
    NOT_FIND_MEMBER_EMAIL("존재하지 않는 이메일 입니다."),
    OVERLAP_MEMBER_EMAIL("이미 가입된 이메일 입니다."),
    WRONG_MEMBER_PASSWORD("비밀번호가 일치하지 않습니다."),
    NOT_MATCH_MEMBER_FLAG("회원 구분이 일치하지 않습니다."),

    // 파트너쉽
    NOT_FIND_APPLY_PARTNERSHIP("파트너쉽 신청하지 않은 회원이 있습니다."),

    // 매니저
    NOT_FIND_RESTAURANT_ID("존재하지 않는 식당 입니다."),
    OVERLAP_RESTAURANT_NAME("이미 등록된 상호명 입니다."),



    // 예약
    CHECK_EMPTY_SEATS("예약 인원수가 초과 했습니다."),
    BEFORE_RESERVATION_DATE("과거의 날짜는 예약할 수 없습니다."),
    TODAY_NOT_RESERVATION("당일 예약은 불가능합니다."),
    FIND_ONE_HOUR_RESERVATION("1시간 간격으로 예약할 수 있습니다."),
    BEFORE_APPROVAL_RESERVATION_DATE("과거의 예약은 승인처리할 수 없습니다."),
    BEFORE_REFUSE_RESERVATION_DATE("과거의 예약은 승인처리할 수 없습니다."),
    BEFORE_CANCEL_RESERVATION_DATE("과거의 예약은 취소할 수 없습니다."),
    NOT_MATCH_RESTAURANT_ID("비예약 정보가 있습니다."),
    NOT_FIND_RESERVATION("예약하신 정보를 확인해 주세요."),


    // 토큰 예외
    TOKEN_ERROR("토큰 애러");


    private final String MESSAGE;
}
