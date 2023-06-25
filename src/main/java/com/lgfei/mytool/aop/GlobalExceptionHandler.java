package com.lgfei.mytool.aop;

import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.lgfei.mytool.exception.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    /**
     * 限流异常
     * @param ex
     * @return
     */
    @ExceptionHandler(FlowException.class)
    public ResponseEntity flowExceptionHandler(FlowException ex) {
        log.error("限流异常: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(ex.getMessage());
    }

    /**
     * 通用异常
     * @param ex
     * @return
     */
    @ExceptionHandler(CommonException.class)
    public ResponseEntity CommonExceptionHandler(CommonException ex) {
        log.error("处理异常: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(ex.getExpMsg());
    }
}
