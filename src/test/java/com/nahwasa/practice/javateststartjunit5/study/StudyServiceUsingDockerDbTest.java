package com.nahwasa.practice.javateststartjunit5.study;

import com.nahwasa.practice.javateststartjunit5.domain.Member;
import com.nahwasa.practice.javateststartjunit5.domain.Study;
import com.nahwasa.practice.javateststartjunit5.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class StudyServiceUsingDockerDbTest {

    /**
     * 메인은 포스트그레를 쓰고, 테스트는 h2를 쓸 때, DB가 다름으로써 트랜잭션 Isolation 레벨 등이 달라 차이점이 생길 가능성이 있음.
     * 그러므로 테스트에서도 동일하게 포스트그레 DB를 쓰려고 함.
     * 도커에 DB를 띄우고 (docker-scripts.sh) 그걸 가지고 테스트를 한 내용임.
     * 도커를 가지고 DB를 관리해야되서 번거로울 수 있음.
     */

    @Mock MemberService memberService;
    @Autowired StudyRepository studyRepository;

    @Test
    void createNewStudy() {
        /*
        GIVEN
         */
        StudyService studyService = new StudyService(memberService, studyRepository);
        assertThat(studyService).isNotNull();

        Member member = new Member();
        member.setId(1L);
        member.setEmail("nahwasa@email.com");

        Study study = new Study(10, "테스트");

        given(memberService.findById(1L)).willReturn(Optional.of(member));

        /*
        WHEN
         */
        studyService.createNewStudy(1L, study);


        /*
        THEN
         */
        assertThat(study.getOwnerId()).isEqualTo(1L);

        // StudyService에 추가로 notify 기능이 들어갔는데, stubbing 으로 뭔가 처리할수 있는게 없음. 대신 이렇게 불렸는지 확인 가능.
        verify(memberService, times(1)).notify(any());  //memberService에서 notify가 1번 불렸어야 한다.
        verify(memberService, never()).validate(any());  //memberService에서 위 테스트 시 validate는 호출되지 않았어야 한다.
    }

    @DisplayName("다른 사용자가 볼 수 있도록 스터디를 공개한다.")
    @Test
    void openStudy() {
        /*
        GIVEN
         */
        StudyService studyService = new StudyService(memberService, studyRepository);
        Study study = new Study(10, "더 자바, 테스트");
        assertThat(study.getOpenedDateTime()).isNull(); // 아래쪽에서 null이 아닌지 테스트할꺼니 여기선 null인지 확인해둠.

        /*
        WHEN
         */
        studyService.openStudy(study);

        /*
        THEN
         */
        // 요구사항: study의 status가 OPENED로 변경됐는지 확인
        assertThat(study.getStatus()).isEqualTo(StudyStatus.OPENED);

        // 요구사항: study의 openedDataTime이 null이 아닌지 확인
        assertThat(study.getOpenedDateTime()).isNotNull();

        // 요구사항: memberService의 notify(study)가 호출 됐는지 확인.
        then(memberService).should().notify(study);
    }

}