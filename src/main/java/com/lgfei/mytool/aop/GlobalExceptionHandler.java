package com.lgfei.mytool.aop;

import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.lgfei.mytool.exception.CommonException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 限流异常
     * @param ex
     * @return
     */
    @ExceptionHandler(FlowException.class)
    public ResponseEntity flowExceptionHandler(FlowException ex) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(ex.getMessage());
    }

    /**
     * 通用异常
     * @param ex
     * @return
     */
    @ExceptionHandler(CommonException.class)
    public ResponseEntity CommonExceptionHandler(CommonException ex) {
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(ex.getMessage());
    }
}
