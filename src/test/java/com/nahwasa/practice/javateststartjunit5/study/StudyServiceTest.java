package com.nahwasa.practice.javateststartjunit5.study;

import com.nahwasa.practice.javateststartjunit5.domain.Member;
import com.nahwasa.practice.javateststartjunit5.domain.Study;
import com.nahwasa.practice.javateststartjunit5.member.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudyServiceTest {
    /**
     * 모든 Mock 객체의 기본 행동
     * - 리턴값이 있을 경우 null 리턴 (Optional 타입은 Optional.empty 리턴)
     * - primitive 타입은 기본 primitive 값 리턴
     * - 콜렉션은 비어있는 콜렉션 리턴
     * - void 메소드는 예외를 던지지 않고 아무런 일도 발생하지 않는다.
     *
     * 현재는 MemberService와 StudyRepository의 구혅체가 없으므로 테스트하려면 Mock을 쓰는 수밖에 없음.
     * 구현체가 있더라도 StudyService쪽의 코드 자체만 테스트하고 싶다면 이렇게 Mock으로 주입해주면 됨.
     * 테스트 전체에 걸쳐서 쓸때는 직전 커밋처럼 인스턴스 변수로 하고, 이 함수내에서만 쓰려면 이렇게 하면 됨.
     */

    @Test
    void memberServiceTestWithMock(@Mock MemberService memberService,
                        @Mock StudyRepository studyRepository) {
        StudyService studyService = new StudyService(memberService, studyRepository);
        assertThat(studyService).isNotNull();

        Member member = new Member();
        member.setId(1L);
        member.setEmail("nahwasa@gmail.com");

        Member member2 = new Member();
        member2.setId(10L);
        member2.setEmail("nahwasa@gmail.com");


        /*
        Mock Stubbing
         */
        when(memberService.findById(1L))   // 이게 호출되면 (any() 를 넣으면 아무거나 넣으면이 됨)
                .thenReturn(Optional.of(member));   // 이렇게 리턴해줘라.

        when(memberService.findById(0L))
                .thenThrow(new RuntimeException());

        when(memberService.findById(argThat(someLong -> someLong > 1L)))
                .thenReturn(Optional.of(member2));

        doThrow(new IllegalArgumentException()).when(memberService).validate(1L);   // 이렇게 쓸 땐 when 밖에 memberService의 함수임.


        Optional<Member> findById = memberService.findById(1L);
        assertThat(findById.get().getEmail()).isEqualTo("nahwasa@gmail.com");

        Optional<Member> findById2 = memberService.findById(6L);
        assertThat(findById2.get().getId()).isEqualTo(10L);

        assertThatThrownBy(() -> memberService.findById(0L))
                .isExactlyInstanceOf(RuntimeException.class);   // 정확히 이거

        assertThatThrownBy(() -> memberService.validate(1L))
                .isInstanceOf(Exception.class); // 부모도 포함
    }

    @Test
    void memberServiceTestWithChainedMock(@Mock MemberService memberService,
                        @Mock StudyRepository studyRepository) {
        StudyService studyService = new StudyService(memberService, studyRepository);
        assertThat(studyService).isNotNull();

        Member member = new Member();
        member.setId(1L);
        member.setEmail("nahwasa@gmail.com");

        /*
        Mock Stubbing
         */
        when(memberService.findById(any()))
                .thenReturn(Optional.of(member))    // 처음 호출 시
                        .thenThrow(new RuntimeException())  // 두번째 호출 시
                                .thenReturn(Optional.empty());  //세번째 호출 시

        assertThat(memberService.findById(1L).get().getEmail()).isEqualTo("nahwasa@gmail.com");
        assertThatThrownBy(() -> memberService.findById(2L))
                .isExactlyInstanceOf(RuntimeException.class);
        assertThat(memberService.findById(3L)).isEqualTo(Optional.empty());
    }

    @Test
    void createNewStudy(@Mock MemberService memberService,
                        @Mock StudyRepository studyRepository) {
        StudyService studyService = new StudyService(memberService, studyRepository);
        assertThat(studyService).isNotNull();

        Member member = new Member();
        member.setId(1L);
        member.setEmail("nahwasa@email.com");

        Study study = new Study(10, "테스트");

        // 요구사항: memberService 객체에 findById 메소드를 1L 값으로 호출하면 member 객체를 리턴하도록 Stubbing
        when(memberService.findById(1L)).thenReturn(Optional.of(member));

        // 요구사항 : studyRepository 객체에 save 메소드를 study 객체로 호출하면 study 객체 그대로 리턴하도록 Stubbing
        when(studyRepository.save(argThat(param -> param instanceof Study))).thenAnswer(param -> param.getArgument(0));
        // 요구사항 보고 위처럼 생각했는데, 강의보니 그냥 아래처럼 하면 됬음.
        // when(studyRepository.save(study)).thenReturn(study);

        studyService.createNewStudy(1L, study);

        assertThat(study.getOwner()).isEqualTo(member);
    }

}