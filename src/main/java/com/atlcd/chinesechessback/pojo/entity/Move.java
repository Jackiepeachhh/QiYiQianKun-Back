package com.atlcd.chinesechessback.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName move
 */
@TableName(value ="move")
@Data
public class Move implements Serializable {
    private Integer id;

    private Integer gameId;

    private Integer step;

    private String movePiece;

    private String eatPiece;

    private Integer fromX;

    private Integer fromY;

    private Integer toX;

    private Integer toY;

    private String message;

    private static final long serialVersionUID = 1L;
}