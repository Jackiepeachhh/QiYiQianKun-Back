package com.atlcd.chinesechessback.pojo.room;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserState implements Serializable {
    private Integer id;

    private String username;

    private Integer winGame;

    private Integer loseGame;

    private Boolean ready;
}
