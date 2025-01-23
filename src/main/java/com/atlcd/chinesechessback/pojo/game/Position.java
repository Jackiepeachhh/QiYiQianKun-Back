package com.atlcd.chinesechessback.pojo.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
public class Position implements Serializable {
    private Integer x;
    private Integer y;
}
