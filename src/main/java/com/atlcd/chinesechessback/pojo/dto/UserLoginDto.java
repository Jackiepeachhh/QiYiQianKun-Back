package com.atlcd.chinesechessback.pojo.dto;

import lombok.Data;

@Data
public class UserLoginDto {
    private String username;
    private String passwordMd5;
}
