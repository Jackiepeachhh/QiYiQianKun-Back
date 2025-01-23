package com.atlcd.chinesechessback.pojo.dto;

import lombok.Data;

@Data
public class UserRegisterDto {
    private String username;
    private String passwordMd5;
}
