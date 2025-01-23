package com.atlcd.chinesechessback.pojo.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class Piece implements Serializable {
    public String name;
    public Boolean isRed;

    public Piece(){

    }
    public Piece(String name){
        this.name = name;
        this.isRed =Character.isLowerCase(name.charAt(0));
    }
}

