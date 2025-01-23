package com.atlcd.chinesechessback.common.exception.enumeration;

import com.atlcd.chinesechessback.common.result.ResultCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoomCodeEnum  implements ResultCodeEnum {

    NOT_EXIST("A0000","房间不存在");


    private final String code;
    private final String message;
}
