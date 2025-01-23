package com.atlcd.chinesechessback.pojo.message;

import com.atlcd.chinesechessback.pojo.room.PlayingGame;
import lombok.Data;

import java.io.Serializable;

@Data
public class GameStartMessage implements Serializable {
    private Boolean isPlaying;
}
