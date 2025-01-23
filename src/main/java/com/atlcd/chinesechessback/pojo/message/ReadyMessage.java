package com.atlcd.chinesechessback.pojo.message;

import lombok.Data;

import java.io.Serializable;

@Data
public class ReadyMessage implements Serializable {
    private Boolean isRed;
}
