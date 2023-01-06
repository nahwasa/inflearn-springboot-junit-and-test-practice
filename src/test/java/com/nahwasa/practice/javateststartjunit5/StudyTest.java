package com.nahwasa.practice.javateststartjunit5;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

/**
 * Run을 gradle로 실행 시 Disabled에 써둔 Disabled 이유가 화면상에 출력되지 않고,
 * 'BUILD SUCCESSFUL in 1s' 등과 같은 그래들 메시지도 보인다.
 * 테스트 내용만 보고 싶고, Tags에 따라 실행하고 싶다면 JUnit으로 Run 해야 한다.
 *
 * JUnit으로 실행 시 Tags에 맞춰 실행하려면 edit configuration에서 JUnit으로 된 run config를 class에서 tags로
 * 변경해서 tag 설정해둔걸 넣어주면 된다.
 *
 * gradle로 실행 시 Tags에 맞춰 실행하려면 build.gradle 맨 아래쪽에 작성한 것 처럼 작성 가능하다.
 * 저걸로 ./gradlew basicTest 처럼 테스트 가능하므로 CI 서버에서도 테스트 가능.
 * (예를들어 통합테스트는 CI 서버에서만 돌리고 싶다면 별도로 integrationTest 라고 만들거나 하면 됨)
 */
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)   // 함수명에 언더바 붙은걸 공백으로 변환하는 규칙지정
@DisplayName("'테스트' 스터디용 Study 클래스는")
class StudyTest {

    @NestedBasicTest    // Custom Tag
    class Study_creation {
        @Test  // Custom Tag로 사용
        @DisplayName("초기 상태값(StudyStatus)은 INIT 이어야 한다.")
        void default_status_is_draft() {
            Study study = new Study();
            // 첫번째 인자가 expected, 두번째가 actual 이므로 예상 기대값을 첫 번째 인자로 넣을 것.
            // 람다식으로 작성하지 않을 시 하나의 String 형태가 아닌 이하와 같은 형태에 대해 매번 String 연산을 하게 되므로
            // 비효율적임. 람다식으로 작성해야 실패시에만 String 연산이 진행됨. 람다식의 원본은 assertEquals의 기존 3번째 인자인
            // new Supplier<String>() 임.
            // 즉 그냥 하나의 String으로 된 메시지면 람다식으로 안적어도 되지만, 연산이 들어가면 람다식으로 작성할 것.
            assertEquals(StudyStatus.INIT, study.getStatus(), () -> "Study 생성시 초기값은 " + StudyStatus.INIT + " 이여야 함.");
        }

        @Test
        @DisplayName("null이 아니어야 한다") // DisplayName 지정 시 함수명으로 표기 안됨
        void is_not_null() {
            Study study = new Study();
            assertNotNull(study);
        }

        @Test
        @EnabledOnJre(JRE.JAVA_17)
        @DisplayName("자바 버전이 17이면 실행한다.")
        void conditional_test_jupyter_jre_17_check() {
            Study study = new Study();
            assertNotNull(study);
        }

        @Test
        @DisplayName("스터디 최대 참가 인원은 0보다 커야 한다.")
        void maximum_limit_is_over_zero() {
            Study study = new Study();
            assertTrue(study.getLimit() > 0, () -> "스터디 참가 인원은 0 이상이어야 함.");
        }

        @Test
        @DisplayName("원하는 익셉션이 발생해야 한다.")
        void runtime_exception_is_occur() {
            Study study = new Study();
            RuntimeException exception =
                assertThrows(RuntimeException.class, () -> study.makeException(), () -> "makeException 실행 시 RuntimeException이 발생해야 함.");
            String message = exception.getMessage();
            assertEquals(StudyExceptionCode.E001.name(), message, "익셉션 메시지는 E001 이어야 한다.");
        }

        @Test
        @DisplayName("1부터 100,000까지의 짝수의 합은 10ms 이내로 획득 가능해야 한다.")
        void sum_of_1_to_100000_under_10ms() {
            Study study = new Study();
            long sum = assertTimeout(   // assertTimeout은 시간 지나도 일단 결과 얻음. assertTimeoutPreemptively는 시간 지나면 멈춤.
                    Duration.ofMillis(10),
                    () -> study.sumEvenNumbersUnderN(100000),
                    "1부터 100000까지의 짝수의 합은 10ms 이내로 획득 가능해야 한다."
            );
            assertEquals(2500050000l, sum, "1부터 100,000까지의 짝수의 합은 2,500,050,000 이어야 한다.");
        }
    }


    @NestedConditionalTest  // Custom Tag
    class Study_creation_conditional {
        @Test
        @DisplayName("JAVA_HOME 기준으로 JDK가 corretto-17.* 이어야만 수행한다.")
        void conditional_test_java_home_is_corretto_17() {
            String javaHome = System.getenv("JAVA_HOME");
            assumeTrue(javaHome!=null && javaHome.contains("corretto-17"));

            Study study = new Study();
            assertNotNull(study);
        }

        @Test
        @DisplayName("JAVA_HOME 기준으로 JDK가 corretto-17.* 일 때와 아닐 때 다른 테스트를 수행한다.")
        void conditional_test_java_home_is_corretto_17_or_not() {
            String javaHome = System.getenv("JAVA_HOME");
            Study study = new Study();
            boolean isCorretto17 = javaHome!=null && javaHome.contains("corretto-17");

            assumingThat(isCorretto17, () -> {
                assertTrue(study.getLimit() > 0, () -> "corretto-17이라면 스터디 참가 인원은 0 이상이어야 함.");
            });

            assumingThat(!isCorretto17, () -> {
                assertTrue(study.getLimit() > 2, () -> "corretto-17이라면 스터디 참가 인원은 2 이상이어야 함.");
            });
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = "^.*(corretto-17).*")  //matches는 정규표현식 가능
        @DisplayName("어노테이션으로 환경변수를 판단해서 corretto-17 일 경우 수행한다.")
        void conditional_test_java_home_is_corretto_17_with_anotation() {
            Study study = new Study();
            assertNotNull(study);
        }

        @Test
        @DisabledOnJre(JRE.JAVA_8)
        @DisplayName("자바 버전이 8이면 실행하지 않는다.")
        void conditional_test_jupyter_jre_not_8_check() {
            Study study = new Study();
            assertNotNull(study);
        }

        @Test
        @EnabledOnJre({JRE.JAVA_17, JRE.JAVA_19})
        @EnabledOnOs({OS.MAC, OS.WINDOWS})
        @DisplayName("자바 버전이 17 또는 19이고, OS가 MAC이나 WINDOWS면 실행한다.")
        void conditional_test_jupyter_annotation() {
            Study study = new Study();
            assertNotNull(study);
        }

        @Test
        @Disabled("일부러 실패하게 해둔 테스트이므로 Disabled")
        @DisplayName("한꺼번에 여럿 수행하는 경우 1 - assertAll 미사용 (비추)")
        void assert_all_1() {
            Study study = new Study();
            // 이 경우 실패 시 중간에서 멈추므로 하나밖에 확인이 안된다. 위처럼 함수별로 나눠서 리포트에 집중하던지, 아니면 아래 '2' 방식으로
            // 처리해야 한다. 이하 2, 3번째 테스트를 일부러 실패하게 해놓은 것.
            assertNotNull(study);
            assertEquals(StudyStatus.ENDED, study.getStatus(), () -> "Study 생성시 초기값은 " + StudyStatus.ENDED + " 이여야 함.");
            assertTrue(study.getLimit() > 5, () -> "스터디 참가 인원은 5 이상이어야 함.");
        }

        @Test
        @Disabled("일부러 실패하게 해둔 테스트이므로 Disabled")
        @DisplayName("한꺼번에 여럿 수행하는 경우 2 - assertAll로 묶는 경우")
        void assert_all_2() {
            Study study = new Study();
            // assertAll로 묶을 시, Excutable이 각 assert가 되며 람다식으로 넘길 수 있다. 이 경우 '1'과 달리 하나가 실패했다고 멈추지않고
            // 전부 실행한 후 틀린 테스트를 모두 찾아준다. 이하 2,3번째 테스트를 일부러 실패하게 해놓은 것.
            assertAll(  // TODO assertSoftly로 변경해보기.
                    () -> assertNotNull(study),
                    () -> assertEquals(StudyStatus.ENDED, study.getStatus(), () -> "Study 생성시 초기값은 " + StudyStatus.ENDED + " 이여야 함."),
                    () -> assertTrue(study.getLimit() > 5, () -> "스터디 참가 인원은 5 이상이어야 함.")
            );
        }

        @Test
        void create_function_name_report() {
            // DisplayName이 없다면 함수명이 기본 표기. DisplayName이 더 나은듯.
            // 카멜케이스 보다는 언더바 표기가 더 낫다고 함. DisplayNameGenerator.ReplaceUnderscores과 합쳐지니 더 그럴듯.
            Study study = new Study();
            assertNotNull(study);
            System.out.println("createMore");
        }

        @BeforeAll  // 모든 테스트 시작 전 1번
        @Tag("before_and_after")
        static void beforeAll() {
            System.out.println("beforeAll");
        }

        @AfterAll   // 모든 테스트 시작 후 1번
        @Tag("before_and_after")
        static void afterAll() {
            System.out.println("afterAll");
        }

        @BeforeEach // 모든 테스트 시작 직전마다 실행
        @Tag("before_and_after")
        void beforeEach() {
            System.out.println("beforeEach");
        }

        @AfterEach  // 모든 테스트 시작 직후마다 실행
        @Tag("before_and_after")
        void afterEach() {
            System.out.println("afterEach");
        }
    }
}