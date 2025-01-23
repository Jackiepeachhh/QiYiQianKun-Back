package com.atlcd.chinesechessback.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "lcd.cors")
@Data
public class CorsProperties {
    private List<String> allowOrigins;
}
