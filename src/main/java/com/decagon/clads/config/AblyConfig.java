package com.decagon.clads.config;

import io.ably.lib.realtime.Channel;
import io.ably.lib.types.Message;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import io.ably.lib.realtime.AblyRealtime;
import io.ably.lib.rest.AblyRest;
import io.ably.lib.types.AblyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AblyConfig {
    @Value( "${ably.apiKey}" )
    public String apiKey;

    @Bean
    public AblyRealtime ablyRealtime() {
        try {

            return new AblyRealtime(apiKey);
        } catch (AblyException exception) {
            return null;
        }
    }
    @Bean
    public Channel getChannel() throws AblyException {
        Channel channel = ablyRealtime().channels.get("cladchat");
//        Channel.MessageListener listener;
//        listener = new Channel.MessageListener() {
//            @SneakyThrows
//            @Override
//            public void onMessage(Message message) {
//                log.info("Message from phone {}", message.data);
//            }};
//        channel.subscribe("1", listener);
        return channel;
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
