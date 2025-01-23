package com.atlcd.chinesechessback.pojo.vo;

import com.atlcd.chinesechessback.pojo.room.UserState;
import lombok.Data;

import java.util.List;

@Data
public class RoomViewVo {
    private Integer roomId;

    private UserState redPlayer;

    private UserState blackPlayer;

    private Integer viewNum;

    private Integer stepNum;
}
