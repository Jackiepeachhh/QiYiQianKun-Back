package com.atlcd.chinesechessback.common.exception.enumeration;

import com.atlcd.chinesechessback.common.result.ResultCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserCodeEnum implements ResultCodeEnum {

    NOT_LOGIN_ERROR("A0000","用户未登录或登录认证失效"),
    USER_NOT_EXIST_ERROR("A0002","用户不存在"),
    USER_EXIST_ERROR("A0003","用户已存在"),
    USER_OLD_PASSWORD_ERROR("A0004","原密码错误"),
    NOT_LOCKED_ERROR("A0005","用户未封禁"),
    USER_LOGIN_ERROR("A0005", "学号或密码错误");


    private final String code;
    private final String message;
}
