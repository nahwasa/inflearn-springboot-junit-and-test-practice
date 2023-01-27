package com.nahwasa.practice.javateststartjunit5.study;

import com.nahwasa.practice.javateststartjunit5.domain.Member;
import com.nahwasa.practice.javateststartjunit5.domain.Study;
import com.nahwasa.practice.javateststartjunit5.member.MemberService;

import java.util.Optional;

public class StudyService {

    private final MemberService memberService;  // 현재 구현체 없음
    private final StudyRepository repository;   // 현재 구현체 없음

    public StudyService(MemberService memberService, StudyRepository repository) {
        assert memberService != null;
        assert repository != null;
        this.memberService = memberService;
        this.repository = repository;
    }

    public Study createNewStudy(Long memberId, Study study) {
        Optional<Member> member = memberService.findById(memberId);
        study.setOwner(member.orElseThrow(() -> new IllegalArgumentException("Member doesn't exist for id: '" + memberId + "'")));
        return repository.save(study);
    }

}
