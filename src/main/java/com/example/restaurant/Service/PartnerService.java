package com.example.restaurant.Service;

import com.example.restaurant.domain.dto.MemberDto;
import com.example.restaurant.domain.entity.Member;
import com.example.restaurant.domain.repository.MemberRepository;
import com.example.restaurant.domain.type.UserType;
import com.example.restaurant.exception.FindException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.restaurant.domain.type.UserType.MANAGER;
import static com.example.restaurant.exception.ErrorCode.NOT_FIND_APPLY_PARTNERSHIP;


@Service
@AllArgsConstructor
public class PartnerService {
    private final MemberRepository memberRepository;


    @Transactional
    public List<MemberDto.MemberInfo> approvalPartnerShip(List<MemberDto.MemberId> memberIdList) {
        List<Member> memberList = new ArrayList<>();
        for (MemberDto.MemberId item : memberIdList) {
            Member member = memberRepository.findByMemberIdAndMemberFlag(item.getMemberId(), UserType.APPLY)
                    .orElseThrow(() -> new FindException(NOT_FIND_APPLY_PARTNERSHIP));

            member.setMemberFlag(MANAGER);
            memberList.add(member);
        }
        return memberList.stream().map(MemberDto.MemberInfo::search).collect(Collectors.toList());
    }
}
