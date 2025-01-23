package com.atlcd.chinesechessback.pojo.vo;

import lombok.Data;

@Data
public class GameInfoVo {
    private Integer id;

    private String red;

    private String black;

    private Boolean isRed;

    private Integer result;

    private Integer stepNum;

    private String reason;
}
