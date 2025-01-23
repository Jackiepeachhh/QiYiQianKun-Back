package com.atlcd.chinesechessback.common.exception.enumeration;

import com.atlcd.chinesechessback.common.result.ResultCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SystemCodeEnum implements ResultCodeEnum {
    SYSTEM_ERROR("B0001", "系统执行出错"),
    DB_ERROR("B0002","数据库错误"),
    UPLOAD_FAILED("B1003","上传失败");

    private final String code;
    private final String message;
}
