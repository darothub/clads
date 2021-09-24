package com.decagon.clads.config;

import com.google.api.client.util.Value;
import io.ably.lib.realtime.AblyRealtime;
import io.ably.lib.rest.AblyRest;
import io.ably.lib.types.AblyException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AblyConfig {
    @Value( "${ably.apiKey}" )
    private String apiKey;

    @Bean
    public AblyRealtime ablyRealtime() {
        try {
            return new AblyRealtime(apiKey);
        } catch (AblyException exception) {
            return null;
        }
    }
    @Bean
    public AblyRest ablyRest() {
        try {
            return new AblyRest(apiKey);
        } catch (AblyException exception) {
            return null;
        }
    }
}
