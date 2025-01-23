package com.atlcd.chinesechessback.common.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "lcd.jwt")
@Data
public class JwtProperties {

    /**
     * 用户端微信用户生成jwt令牌相关配置
     */
    private String userSecretKey;
    @Value("#{${lcd.jwt.user-ttl-hours} * 60 * 60 * 1000}")
    private long userTtl;
    private String userTokenName;

}
