package com.atlcd.chinesechessback.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


@Component
@Getter
public class StompConfig {

    @Autowired
    private Environment environment;

    private int disconnectDelay;

    private int httpMessageCacheSize;

    private int streamBytesLimit;

    @PostConstruct
    public void init() {
        this.disconnectDelay = environment
                .getProperty("lcd.stomp.disconnectDelay", Integer.class, 900);

        this.httpMessageCacheSize = environment
                .getProperty("lcd.stomp.httpMessageCacheSize", Integer.class, 1000);

        this.streamBytesLimit = environment
                .getProperty("lcd.stomp.streamBytesLimit", Integer.class, 524288);
    }

}
