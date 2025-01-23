package com.atlcd.chinesechessback.pojo.message;

import lombok.Data;

import java.io.Serializable;

@Data
public class RequestMessage implements Serializable {
    private Integer type;
    private String sender;
    private String receiver;
}
