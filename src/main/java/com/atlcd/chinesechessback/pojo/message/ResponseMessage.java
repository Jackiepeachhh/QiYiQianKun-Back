package com.atlcd.chinesechessback.pojo.message;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResponseMessage implements Serializable {
    private Integer type;
    private Boolean result;
    private String sender;
    private String receiver;
}
