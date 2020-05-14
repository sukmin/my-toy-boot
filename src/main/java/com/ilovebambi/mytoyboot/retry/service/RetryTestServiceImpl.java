package com.ilovebambi.mytoyboot.retry.service;

import lombok.extern.java.Log;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.context.RetryContextSupport;
import org.springframework.retry.policy.CompositeRetryPolicy;
import org.springframework.retry.policy.RetryContextCache;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.policy.TimeoutRetryPolicy;
import org.springframework.retry.support.RetrySynchronizationManager;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * https://github.com/spring-projects/spring-retry 참고
 */
@Service
@Log
public class RetryTestServiceImpl implements RetryTestService {

    public static final String COUNT = "count";

    @Override
    @Retryable(maxAttempts = 2, backoff = @Backoff(delay = 3000, maxDelay = 5000))
    public String getMyString() {

        log.info("getMyString 메소드 진입");

        throw new RuntimeException("my string fail");
        //return "I am retry";
    }

    @Override // stateful 필드를 어떻게 사용하는지 모르겠음
    @Retryable(maxAttempts = 5, stateful = true)
    public String getMyStringWithContext(int number) {
        log.info("getMyStringWithContext 메소드 진입");
        throw new RuntimeException("my string fail");
        //return "I am retry";
    }

    @Recover //리턴 타입까지 같아야 정상 동작한다.
    public String recover(RuntimeException e) {
        log.info("recover 테스트 " + e.getMessage());
        return "recover success";
    }

    @Override // 위에서 사용한 어노테이션들은 사실 내부에서 아래처럼 템플릿을 시용해 작업한다.
    public String useTemplate() {

        RetryTemplate template = new RetryTemplate();

        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy(5);
        TimeoutRetryPolicy policy = new TimeoutRetryPolicy();
        policy.setTimeout(1000L);

        CompositeRetryPolicy compositeRetryPolicy = new CompositeRetryPolicy();
        compositeRetryPolicy.setPolicies(new RetryPolicy[]{simpleRetryPolicy, policy});

        template.setRetryPolicy(compositeRetryPolicy);

        return template.execute(context -> {
            log.info("template execute 내부");
            Object count = context.getAttribute(COUNT);
            if (Objects.isNull(count)) {
                log.info("execute count 1");
                context.setAttribute(COUNT, 1L);
            } else {
                long currentCount = ((Long) count) + 1L;
                log.info("execute count " + currentCount);
                context.setAttribute(COUNT, currentCount);
            }
            throw new RuntimeException("template fail");
        });
    }
}
