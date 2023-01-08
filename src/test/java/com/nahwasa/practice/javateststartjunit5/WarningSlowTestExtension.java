package com.nahwasa.practice.javateststartjunit5;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;

public class WarningSlowTestExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {
    private int DURATION_LIMIT_MS;

    public WarningSlowTestExtension() {
        this(10);
    }

    public WarningSlowTestExtension(int DURATION_LIMIT_MS) {
        this.DURATION_LIMIT_MS = DURATION_LIMIT_MS;
    }

    private static ExtensionContext.Store getStore(ExtensionContext context) {
        String className = context.getRequiredTestClass().getName();
        String methodName = context.getRequiredTestMethod().getName();
        ExtensionContext.Store store = context.getStore(ExtensionContext.Namespace.create(className, methodName));
        return store;
    }
    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        ExtensionContext.Store store = getStore(context);   // 클래스명과 메소드명으로 store 획득
        store.put("START_AT", System.currentTimeMillis());  // 시작 시간을 저장
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        String methodName = context.getRequiredTestMethod().getName();
        RepeatedTest repeatedTestAnnotation = context.getRequiredTestMethod().getAnnotation(RepeatedTest.class);
        ParameterizedTest parameterizedTestAnnotation = context.getRequiredTestMethod().getAnnotation(ParameterizedTest.class);

        ExtensionContext.Store store = getStore(context);
        long startAt = store.remove("START_AT", long.class);    // beforeTestExecution에서 넣은 시작시간을 꺼내옴
        long duration = System.currentTimeMillis() - startAt;   // 얼마나 걸렸는지
        if (duration > DURATION_LIMIT_MS && repeatedTestAnnotation == null && parameterizedTestAnnotation == null)  // 지정한 시간이 넘는데, Reapeated나 Parameterized가 아니라면 메시지 출력
            System.out.println(methodName + " 테스트는 " + DURATION_LIMIT_MS + "ms 초과로 시간이 걸립니다.");
    }
}
