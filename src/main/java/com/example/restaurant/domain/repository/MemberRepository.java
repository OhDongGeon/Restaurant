package com.example.restaurant.domain.repository;

import com.example.restaurant.domain.entity.Member;
import com.example.restaurant.domain.type.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByMemberEmail(String memberEmail);

    Optional<Member> findByMemberId(Long memberId);

    Optional<Member> findByMemberEmail(String memberEmail);

    Optional<Member> findByMemberIdAndMemberFlag(Long memberId, UserType memberFlag);
}
