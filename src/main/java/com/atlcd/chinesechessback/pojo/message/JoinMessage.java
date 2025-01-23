package com.atlcd.chinesechessback.pojo.message;

import com.atlcd.chinesechessback.pojo.room.UserState;
import lombok.Data;

import java.io.Serializable;

@Data
public class JoinMessage implements Serializable {
    private Boolean isRed;

    private UserState player;
}
