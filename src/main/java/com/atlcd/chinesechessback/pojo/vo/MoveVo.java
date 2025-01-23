package com.atlcd.chinesechessback.pojo.vo;

import com.atlcd.chinesechessback.pojo.game.Piece;
import com.atlcd.chinesechessback.pojo.game.Position;
import lombok.Data;

@Data
public class MoveVo {
    private Position from;
    private Position to;
    private Piece movePiece;
    private Piece eatPiece;
    private String message;
}
