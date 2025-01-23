package com.atlcd.chinesechessback.pojo.message;

import lombok.Data;

import java.io.Serializable;

@Data
public class GameOverMessage implements Serializable {

    private Integer result;

    private String winner;

    private String loser;

    private String reason;
}
