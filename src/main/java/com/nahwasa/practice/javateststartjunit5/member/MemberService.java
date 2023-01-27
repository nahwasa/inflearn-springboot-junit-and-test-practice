package com.nahwasa.practice.javateststartjunit5.member;

import com.nahwasa.practice.javateststartjunit5.domain.Member;

import java.util.Optional;

public interface MemberService {
    Optional<Member> findById(Long memberId);
}
