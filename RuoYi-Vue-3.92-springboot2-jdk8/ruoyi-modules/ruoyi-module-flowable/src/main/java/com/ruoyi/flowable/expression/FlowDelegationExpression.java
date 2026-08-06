package com.ruoyi.flowable.expression;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("flowDelegationExpression")
public class FlowDelegationExpression implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        log.info("代理表达式执行器:{}", execution);
    }
}
