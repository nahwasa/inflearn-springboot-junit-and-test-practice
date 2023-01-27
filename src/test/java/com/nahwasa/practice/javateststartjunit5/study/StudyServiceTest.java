package com.nahwasa.practice.javateststartjunit5.study;

import com.nahwasa.practice.javateststartjunit5.member.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StudyServiceTest {

    @Test
    void createStudyService(@Mock MemberService memberService,
                            @Mock StudyRepository studyRepository) {
        // 현재는 MemberService와 StudyRepository의 구혅체가 없으므로 테스트하려면 Mock을 쓰는 수밖에 없음.
        // 구현체가 있더라도 StudyService쪽의 코드 자체만 테스트하고 싶다면 이렇게 Mock으로 주입해주면 됨.
        // 테스트 전체에 걸쳐서 쓸때는 직전 커밋처럼 인스턴스 변수로 하고, 이 함수내에서만 쓰려면 이렇게 하면 됨.
        StudyService studyService = new StudyService(memberService, studyRepository);

        Assertions.assertThat(studyService).isNotNull();
    }
}