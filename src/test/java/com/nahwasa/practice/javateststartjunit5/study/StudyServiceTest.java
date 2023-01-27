package com.nahwasa.practice.javateststartjunit5.study;

import com.nahwasa.practice.javateststartjunit5.member.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

class StudyServiceTest {

    @Test
    void createStudyService() {
        MemberService memberSerivce = mock(MemberService.class);
        StudyRepository studyRepository = mock(StudyRepository.class);

        StudyService studyService = new StudyService(memberSerivce, studyRepository);

        Assertions.assertThat(studyService).isNotNull();
    }
}