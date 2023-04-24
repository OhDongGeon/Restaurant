package com.example.restaurant.domain.dto;

import com.example.restaurant.domain.entity.Member;
import com.example.restaurant.domain.type.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Locale;

import static com.example.restaurant.domain.type.UserType.USER;


public class MemberDto {
    @Getter
    @Setter
    public static class MemberId {
        private Long memberId;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignUp {
        private String memberEmail;
        private String memberPassword;
        private String memberName;
        private String memberNickName;
        private String memberBirthday;
        private String memberPhone;
        private UserType memberFlag;

        public Member save(MemberDto.SignUp signUp) {
            return Member.builder()
                    .memberEmail(signUp.getMemberEmail().toLowerCase(Locale.ROOT))
                    .memberPassword(signUp.getMemberPassword())
                    .memberName(signUp.getMemberName())
                    .memberNickName(signUp.getMemberNickName())
                    .memberBirthday(signUp.getMemberBirthday())
                    .memberPhone(signUp.getMemberPhone())
                    .memberFlag(USER)
                    .build();
        }
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignIn {
        private String memberEmail;
        private String memberPassword;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    public static class MemberInfo {
        private String memberEmail;
        private String memberName;
        private String memberNickName;
        private String memberBirthday;
        private String memberPhone;
        private UserType memberFlag;

        public static MemberInfo search(Member member) {
            return new MemberInfo(member.getMemberEmail(), member.getMemberName(), member.getMemberNickName(),
                    member.getMemberBirthday(), member.getMemberPhone(), member.getMemberFlag());
        }
    }
}
