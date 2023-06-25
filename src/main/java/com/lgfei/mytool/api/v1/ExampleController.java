package com.lgfei.mytool.api.v1;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.lgfei.mytool.config.SentinelRuleConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "示例接口")
@RestController
@RequestMapping("/v1/example")
public class ExampleController {

    @ApiOperation(value = "测试Sentinel限流")
    @GetMapping("/testSentinel")
    @SentinelResource(value = SentinelRuleConfig.QPS_LIMIT)
    public ResponseEntity<String> testSentinel(){
        return ResponseEntity.ok().body("testSentinel:" + String.valueOf(System.currentTimeMillis()));
    }
}
