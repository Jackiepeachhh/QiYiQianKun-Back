package com.atlcd.chinesechessback.common.exception.enumeration;


import com.atlcd.chinesechessback.common.result.ResultCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonCodeEnum implements ResultCodeEnum {
    REQUEST_PARAM_ERROR("A0000","用户请求参数异常");

    private final String code;
    private final String message;
}
