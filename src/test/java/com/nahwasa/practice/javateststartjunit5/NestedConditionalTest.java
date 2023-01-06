package com.nahwasa.practice.javateststartjunit5;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) // 클래스, 인터페이스, 열거타입에 쓸 수 있다.
@Retention(RetentionPolicy.RUNTIME) // 런타임까지 남는다. (안사라진다)
@Nested
@DisplayName("생성시에 (조건에 따라 동작 안하거나 일부러 실패하게 해둔 테스트)")
@Tag("conditional_test")
public @interface NestedConditionalTest {   // 여러 어노테이션을 조합한 새로운 어노테이션
}
