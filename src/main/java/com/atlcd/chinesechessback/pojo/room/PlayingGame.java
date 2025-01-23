package com.atlcd.chinesechessback.pojo.room;

import com.atlcd.chinesechessback.pojo.entity.Move;
import com.atlcd.chinesechessback.pojo.game.Piece;
import com.atlcd.chinesechessback.pojo.game.Position;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class PlayingGame implements Serializable {
    private Boolean isGameOver;

    private Boolean isRedWin;

    private Boolean isRedTurn;

    private Boolean isGeneralChecking;

    private List<Move> moves;

    private Piece[][] board;

    public static final int ROW = 10;

    public static final int COLUMN = 9;

    public static final String[][] initBoard = {
            {"C0", "M0", "X0", "S0", "J0", "S1", "X1", "M1", "C1"},
            {null, null, null, null, null, null, null, null, null},
            {null, "P0", null, null, null, null, null, "P1", null},
            {"Z0", null, "Z1", null, "Z2", null, "Z3", null, "Z4"},
            {null, null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null, null},
            {"z0", null, "z1", null, "z2", null, "z3", null, "z4"},
            {null, "p0", null, null, null, null, null, "p1", null},
            {null, null, null, null, null, null, null, null, null},
            {"c0", "m0", "x0", "s0", "j0", "s1", "x1", "m1", "c1"}
    };

    public void createInitBoard(String[][] initBoard){
        board = new Piece[ROW][COLUMN];
        for(int i = 0 ; i < ROW ; i++){
            for(int j=0 ; j< COLUMN;j++){
                if(initBoard[i][j] != null){
                    board[i][j] = new Piece();
                    board[i][j].setName(initBoard[i][j]);
                    char firstChar = initBoard[i][j].charAt(0);
                    board[i][j].setIsRed(Character.isLowerCase(firstChar));
                }
            }
        }
    }

    public void move(Position from, Position to){
        board[to.getY()][to.getX()] = board[from.getY()][from.getX()];
        board[from.getY()][from.getX()] = null;
    }

    public void unmove(Move move){
        Piece movePiece = new Piece();
        movePiece.setName(move.getMovePiece());
        movePiece.setIsRed(Character.isLowerCase(move.getMovePiece().charAt(0)));
        board[move.getFromY()][move.getFromY()] = movePiece;
        if(move.getEatPiece()==null){
            board[move.getToY()][move.getToY()] = null;
        }else{
            Piece eatPiece = new Piece();
            eatPiece.setName(move.getEatPiece());
            eatPiece.setIsRed(Character.isLowerCase(move.getEatPiece().charAt(0)));
            board[move.getToY()][move.getToX()] = eatPiece;
        }
    }
}
