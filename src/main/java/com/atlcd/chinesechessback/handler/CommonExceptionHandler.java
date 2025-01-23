package com.atlcd.chinesechessback.handler;



import com.atlcd.chinesechessback.common.exception.BusinessException;
import com.atlcd.chinesechessback.common.exception.enumeration.CommonCodeEnum;
import com.atlcd.chinesechessback.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CommonExceptionHandler {
    /**
     * 处理数据校验异常
     * */
    @ExceptionHandler(BindException.class)
    public Result<Void> handlerBindException(BindException e){
        log.error(e.getMessage(),e);
        return Result.fail(CommonCodeEnum.REQUEST_PARAM_ERROR);
    }

    /**
     * 处理业务异常
     * */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handlerBusinessException(BusinessException e){
        log.error(e.getMessage(),e);
        return Result.fail(e.getResultCodeEnum());
    }

    /**
     * 处理系统异常
     * */
    @ExceptionHandler(Exception.class)
    public Result<Void> handlerException(Exception e){
        log.error(e.getMessage(),e);
        return Result.error();
    }
}
