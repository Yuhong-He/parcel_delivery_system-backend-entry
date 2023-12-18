package com.example.parcel_delivery_systembackendentry.exception;

import com.example.parcel_delivery_systembackendentry.common.Result;
import com.example.parcel_delivery_systembackendentry.enumeration.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result<Object> exceptionHandler(Exception e){
        log.error("Unexpected error: " + e.getMessage());
        for(StackTraceElement s : e.getStackTrace()) {
            log.error("\t" + s);
        }
        return Result.error(e.getMessage(), ResultCodeEnum.FAIL);
    }

}
