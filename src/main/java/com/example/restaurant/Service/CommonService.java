package com.example.restaurant.Service;

import com.example.restaurant.domain.dto.MemberDto;
import com.example.restaurant.domain.entity.Member;
import com.example.restaurant.domain.repository.MemberRepository;
import com.example.restaurant.exception.FindException;
import com.example.restaurant.security.TokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.restaurant.exception.ErrorCode.*;


@Service
@AllArgsConstructor
public class CommonService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;


    // 회원가입
    @Transactional
    public String memberSignUp(MemberDto.SignUp signUp) {
        if (memberRepository.existsByMemberEmail(signUp.getMemberEmail())) {
            throw new FindException(OVERLAP_MEMBER_EMAIL);
        }
        signUp.setMemberPassword(passwordEncoder.encode(signUp.getMemberPassword()));
        memberRepository.save(signUp.save(signUp));

        return "회원가입이 완료 되었습니다.";
    }


    // 로그인 토큰 발행
    public String memberSingIn(MemberDto.SignIn signInDto) {
        Member user = memberRepository.findByMemberEmail(signInDto.getMemberEmail())
                .orElseThrow(() -> new FindException(NOT_FIND_MEMBER_EMAIL));

        if (!passwordEncoder.matches(signInDto.getMemberPassword(), user.getMemberPassword())) {
            throw new FindException(WRONG_MEMBER_PASSWORD);
        }

        return tokenProvider.createToken(user.getMemberId(), user.getMemberEmail(), user.getMemberFlag());
    }


    // 아이디와 이메일로 데이터 찾기
    public Optional<Member> findByIdAndEmail(Long memberId, String memberEmail) {
        return memberRepository.findById(memberId).stream()
                .filter(member -> member.getMemberEmail().equals(memberEmail))
                .findFirst();
    }
}
