package com.atlcd.chinesechessback.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @TableName game
 */
@TableName(value ="game")
@Data
public class Game implements Serializable {
    private Integer id;

    private String red;

    private String black;

    private Integer result;

    private String winner;

    private String loser;

    private String reason;

    private static final long serialVersionUID = 1L;
}