package com.nahwasa.practice.javateststartjunit5;

import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)   // 함수명에 언더바 붙은걸 공백으로 변환하는 규칙지정
@DisplayName("'테스트' 스터디용 Study 클래스는")
class StudyTest {

    @Nested
    @DisplayName("생성시에")
    class Study_creation {
        Study study = new Study();

        @Test
        @DisplayName("null이 아니어야 한다") // DisplayName 지정 시 함수명으로 표기 안됨
        void is_not_null() {
            assertNotNull(study);
        }

        @Test
        @DisplayName("초기 상태값(StudyStatus)은 INIT 이어야 한다.")
        void default_status_is_draft() {
            // 첫번째 인자가 expected, 두번째가 actual 이므로 예상 기대값을 첫 번째 인자로 넣을 것.
            // 람다식으로 작성하지 않을 시 하나의 String 형태가 아닌 이하와 같은 형태에 대해 매번 String 연산을 하게 되므로
            // 비효율적임. 람다식으로 작성해야 실패시에만 String 연산이 진행됨. 람다식의 원본은 assertEquals의 기존 3번째 인자인
            // new Supplier<String>() 임.
            // 즉 그냥 하나의 String으로 된 메시지면 람다식으로 안적어도 되지만, 연산이 들어가면 람다식으로 작성할 것.
            assertEquals(StudyStatus.INIT, study.getStatus(), () -> "Study 생성시 초기값은 " + "INIT" + " 이여야 함.");
        }

        @Test
        @DisplayName("스터디 최대 참가 인원은 0보다 커야 한다.")
        void maximum_limit_is_over_zero() {
            assertTrue(study.getLimit() > 0, () -> "스터디 참가 인원은 0 이상이어야 함.");
        }

        @Test
        @DisplayName("한꺼번에 여럿 수행하는 경우 1 - assertAll 미사용 (비추)")
        void assert_all_1() {
            // 이 경우 실패 시 중간에서 멈추므로 하나밖에 확인이 안된다. 위처럼 함수별로 나눠서 리포트에 집중하던지, 아니면 아래 '2' 방식으로
            // 처리해야 한다. 이하 2, 3번째 테스트를 일부러 실패하게 해놓은 것.
            assertNotNull(study);
            assertEquals(StudyStatus.ENDED, study.getStatus(), () -> "Study 생성시 초기값은 " + StudyStatus.ENDED + " 이여야 함.");
            assertTrue(study.getLimit() > 5, () -> "스터디 참가 인원은 5 이상이어야 함.");
        }

        @Test
        @DisplayName("한꺼번에 여럿 수행하는 경우 2 - assertAll로 묶는 경우")
        void assert_all_2() {
            // assertAll로 묶을 시, Excutable이 각 assert가 되며 람다식으로 넘길 수 있다. 이 경우 '1'과 달리 하나가 실패했다고 멈추지않고
            // 전부 실행한 후 틀린 테스트를 모두 찾아준다. 이하 2,3번째 테스트를 일부러 실패하게 해놓은 것.
            assertAll(
                    () -> assertNotNull(study),
                    () -> assertEquals(StudyStatus.ENDED, study.getStatus(), () -> "Study 생성시 초기값은 " + StudyStatus.ENDED + " 이여야 함."),
                    () -> assertTrue(study.getLimit() > 5, () -> "스터디 참가 인원은 5 이상이어야 함.")
            );
        }

        @Test
        @DisplayName("원하는 익셉션이 발생해야 한다.")
        void runtime_exception_is_occur() {
            RuntimeException exception =
                assertThrows(RuntimeException.class, () -> study.makeException(), () -> "makeException 실행 시 RuntimeException이 발생해야 함.");
            String message = exception.getMessage();
            assertEquals(StudyExceptionCode.E001.name(), message, "익셉션 메시지는 E001 이어야 한다.");
        }

        @Test
        @DisplayName("1부터 100,000까지의 짝수의 합은 10ms 이내로 획득 가능해야 한다.")
        void sum_of_1_to_100000_under_10ms() {
            long sum = assertTimeout(   // assertTimeout은 시간 지나도 일단 결과 얻음. assertTimeoutPreemptively는 시간 지나면 멈춤.
                    Duration.ofMillis(10),
                    () -> study.sumEvenNumbersUnderN(100000),
                    "1부터 100000까지의 짝수의 합은 10ms 이내로 획득 가능해야 한다."
            );
            assertEquals(2500050000l, sum, "1부터 100,000까지의 짝수의 합은 2,500,050,000 이어야 한다.");
        }
    }


    /**
    @Test
    @Disabled // 해당 테스트 넘기고 싶을 때 (비추)
    void create_function_name_report() {
        // DisplayName이 없다면 함수명이 기본 표기. DisplayName이 더 나은듯.
        // 카멜케이스 보다는 언더바 표기가 더 낫다고 함. DisplayNameGenerator.ReplaceUnderscores과 합쳐지니 더 그럴듯.
        Study study = new Study();
        assertNotNull(study);
        System.out.println("createMore");
    }

    @BeforeAll  // 모든 테스트 시작 전 1번
    @Disabled
    static void beforeAll() {
        System.out.println("beforeAll");
    }

    @AfterAll   // 모든 테스트 시작 후 1번
    @Disabled
    static void afterAll() {
        System.out.println("afterAll");
    }

    @BeforeEach // 모든 테스트 시작 직전마다 실행
    @Disabled
    void beforeEach() {
        System.out.println("beforeEach");
    }

    @AfterEach  // 모든 테스트 시작 직후마다 실행
    @Disabled
    void afterEach() {
        System.out.println("afterEach");
    }
    */
}