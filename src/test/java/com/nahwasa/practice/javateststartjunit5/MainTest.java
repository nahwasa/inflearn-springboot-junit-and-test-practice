package com.nahwasa.practice.javateststartjunit5;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
// 함수명에 언더바 붙은걸 공백으로 변환하는 규칙지정
class MainTest {

    @Test
    @DisplayName("생성함") // DisplayName 지정 시 함수명으로 표기 안됨
    void create() {
        Main main = new Main();
        assertNotNull(main);
        System.out.println("create");
    }

    @Test
//    @Disabled // 해당 테스트 넘기고 싶을 때 (비추)
    void create_function_name_report() {
        // DisplayName이 없다면 함수명이 기본 표기. DisplayName이 더 나은듯.
        // 카멜케이스 보다는 언더바 표기가 더 낫다고 함. DisplayNameGenerator.ReplaceUnderscores과 합쳐지니 더 그럴듯.
        Main main = new Main();
        assertNotNull(main);
        System.out.println("createMore");
    }

    @BeforeAll  // 모든 테스트 시작 전 1번
    static void beforeAll() {
        System.out.println("beforeAll");
    }

    @AfterAll   // 모든 테스트 시작 후 1번
    static void afterAll() {
        System.out.println("afterAll");
    }

    @BeforeEach // 모든 테스트 시작 직전마다 실행
    void beforeEach() {
        System.out.println("beforeEach");
    }

    @AfterEach  // 모든 테스트 시작 직후마다 실행
    void afterEach() {
        System.out.println("afterEach");
    }
}