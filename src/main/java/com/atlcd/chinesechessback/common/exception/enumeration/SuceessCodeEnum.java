package com.atlcd.chinesechessback.common.exception.enumeration;


import com.atlcd.chinesechessback.common.result.ResultCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum SuceessCodeEnum implements ResultCodeEnum {

    OK("00000","OK");

    private final String code;
    private final String message;
}
