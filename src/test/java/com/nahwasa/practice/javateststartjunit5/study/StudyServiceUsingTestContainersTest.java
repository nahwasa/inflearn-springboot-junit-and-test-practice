package com.nahwasa.practice.javateststartjunit5.study;

import com.nahwasa.practice.javateststartjunit5.domain.Member;
import com.nahwasa.practice.javateststartjunit5.domain.Study;
import com.nahwasa.practice.javateststartjunit5.member.MemberService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Testcontainers
@ActiveProfiles("test")
class StudyServiceUsingTestContainersTest {

    /**
     * 도커로 직접 띄워서 테스트해보려면 도커까지 관리해줘야함.
     * TestContainers 사용해서 이 부분을 해결 가능.
     * 다만, docker가 있긴 해야함. (윈도우라면 Docker Desktop 실행되있어야함)
     * 그러니 그냥 직접 도커 세팅 안하게 해주는 매크로 정도로 보임.
     *
     * 장점 : 별도로 외부의 docker나 DB 설정 없이 코드만 가지고 테스트 가능
     * 단점 : 많이 느림 ㅠ
     */

    @Mock MemberService memberService;
    @Autowired StudyRepository studyRepository;

    @Container
    private static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer()  // static 없으면 테스트마다 컨테이너 새로 생성
            .withDatabaseName("study");

    @BeforeAll
    static void beforeAll() {
        postgreSQLContainer.start();
    }

    @AfterAll
    static void afterAll() {
        postgreSQLContainer.stop();
    }

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