package com.atlcd.chinesechessback.pojo.message;

import com.atlcd.chinesechessback.pojo.game.Piece;
import com.atlcd.chinesechessback.pojo.game.Position;
import lombok.Data;

import java.io.Serializable;

@Data
public class MoveMessage implements Serializable{
    private Position from;
    private Position to;
    private Piece movePiece;
    private Piece eatPiece;
}
