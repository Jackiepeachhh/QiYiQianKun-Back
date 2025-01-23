package com.atlcd.chinesechessback.common.result;


import com.atlcd.chinesechessback.common.exception.enumeration.SuceessCodeEnum;
import com.atlcd.chinesechessback.common.exception.enumeration.SystemCodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Getter;

import java.util.Objects;

@Getter
public class Result<T> {
    @Schema(description = "错误码，00000-没有错误")
    private String code;

    @Schema(description = "响应消息")
    private String message;

    @Schema(description = "响应数据")
    private T data;

    private Result() {
        this.code = SuceessCodeEnum.OK.getCode();
        this.message = SuceessCodeEnum.OK.getMessage();
    }

    private Result(ResultCodeEnum resultCodeEnum) {
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
    }

    private Result(T data) {
        this();
        this.data = data;
    }

    public static Result<Void> ok() {
        return new Result<>();
    }

    /**
     * 业务处理成功，有数据返回
     */
    public static <T> Result<T> ok(T data) {
        return new Result<>(data);
    }

    /**
     * 业务处理失败
     */
    public static Result<Void> fail(ResultCodeEnum resultCodeEnum) {
        return new Result<>(resultCodeEnum);
    }


    /**
     * 系统错误
     */
    public static Result<Void> error(){
        return new Result<>(SystemCodeEnum.SYSTEM_ERROR);
    }

    /**
     * 判断是否成功
     */
    public boolean isOk() {
        return Objects.equals(this.code, SuceessCodeEnum.OK.getCode());
    }
}
