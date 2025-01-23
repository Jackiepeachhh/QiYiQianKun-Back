package com.atlcd.chinesechessback.pojo.room;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Room implements Serializable {
    private Integer roomId;

    private UserState redPlayer;

    private UserState blackPlayer;

    private List<String> viewers;

    private Boolean isPlaying;

    private PlayingGame playingGame;
}
