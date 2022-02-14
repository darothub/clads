package com.decagon.clads.config;

import io.ably.lib.realtime.Channel;
import io.ably.lib.types.Message;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import io.ably.lib.realtime.AblyRealtime;
import io.ably.lib.rest.AblyRest;
import io.ably.lib.types.AblyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

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
//        Channel.MessageListener m = new Channel.MessageListener() {
//            @Override
//            public void onMessage(Message message) {
//                try {
//                    JSONObject json = new JSONObject((String) message.data);
//                    String sender = json.getString("sender");
//                    String receiver = json.getString("receiver");
//                    String message0 = json.getString("message");
//                    log.info("Ably Message {} says {} to {}", sender, message0, receiver);
//                    channel.publish("onNewMessage", message);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                } catch (AblyException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        String nodes = "onNewMessage";
//        channel.subscribe(nodes, m);
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
