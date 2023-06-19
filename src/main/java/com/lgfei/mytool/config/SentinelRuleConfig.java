package com.lgfei.mytool.config;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SentinelRuleConfig implements InitializingBean {
    @Value("${sentinel.qps.limit:1}")
    private Integer limit;

    public final static String QPS_LIMIT = "concurrent_qps_limit";

    @Override
    public void afterPropertiesSet() throws Exception {
        initFlowQpsRule(QPS_LIMIT);
    }

    private void initFlowQpsRule(String resource) {
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule1 = new FlowRule();
        rule1.setResource(resource);
        rule1.setCount(limit);
        rule1.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rules.add(rule1);
        FlowRuleManager.loadRules(rules);
    }
}
