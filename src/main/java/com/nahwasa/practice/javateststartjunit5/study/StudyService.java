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
        if (member.isPresent()) {
            study.setOwnerId(memberId);
        } else {
            throw new IllegalArgumentException("Member doesn't exist for id: '" + memberId + "'");
        }

        Study newstudy = repository.save(study);
        memberService.notify(newstudy); // 멤버 서비스한테 새로운 스터디가 나왔다고 알려주는 부분 -> 단순 스터빙으론 뭐 할 수 있는게 없음.

        return repository.save(study);
    }

    public Study openStudy(Study study) {
        study.open();
        Study openedStudy = repository.save(study);
        memberService.notify(openedStudy);
        return openedStudy;
    }

}
