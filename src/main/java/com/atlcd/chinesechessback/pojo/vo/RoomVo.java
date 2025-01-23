package com.atlcd.chinesechessback.pojo.vo;

import com.atlcd.chinesechessback.pojo.room.PlayingGame;
import com.atlcd.chinesechessback.pojo.room.UserState;
import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
public class RoomVo {
    private Integer roomId;

    private UserState redPlayer;

    private UserState blackPlayer;

    private List<String> viewers;

    private Boolean isPlaying;

    private PlayingGame playingGame;
}
