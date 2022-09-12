package com.zerogift.batch.application.client;

import com.zerogift.batch.application.client.dto.SlackRequestDto;
import com.zerogift.batch.core.dto.ResultListenerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SlackEvent {
    private final SlackClient slackClient;

    @Value("${slack.channel}")
    private String channel;
    
    public void send(ResultListenerDto resultListenerDto) {
        slackClient.message(channel, SlackRequestDto.from(resultListenerDto));
    }

}
