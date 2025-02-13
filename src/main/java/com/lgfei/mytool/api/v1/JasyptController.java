package com.lgfei.mytool.api.v1;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.lgfei.mytool.config.SentinelRuleConfig;
import com.lgfei.mytool.dto.JasyptDTO;
import com.lgfei.mytool.enums.JasyptAlgorithmEnum;
import com.lgfei.mytool.service.JasyptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lgfei
 * @date 2025/2/13 11:06
 */
@Api(tags = "Jasypt加解密接口")
@RestController
@RequestMapping("/v1/jasypt")
public class JasyptController {

    private final JasyptService service;

    public JasyptController(JasyptService service) {
        this.service = service;
    }

    @ApiOperation(value = "算法列表")
    @GetMapping("/algorithm/list")
    @SentinelResource(value = SentinelRuleConfig.QPS_LIMIT)
    public ResponseEntity<List<String>> listAlgorithm(){
        return ResponseEntity.ok().body(JasyptAlgorithmEnum.getAllCode());
    }

    @ApiOperation(value = "加密")
    @PostMapping("/encrypt")
    @SentinelResource(value = SentinelRuleConfig.QPS_LIMIT)
    public ResponseEntity<String> encrypt(@RequestBody JasyptDTO jasypt){
        return ResponseEntity.ok(service.encrypt(jasypt));
    }

    @ApiOperation(value = "解密")
    @PostMapping("/decrypt")
    @SentinelResource(value = SentinelRuleConfig.QPS_LIMIT)
    public ResponseEntity<String> decrypt(@RequestBody JasyptDTO jasypt){
        return ResponseEntity.ok(service.decrypt(jasypt));
    }
}
