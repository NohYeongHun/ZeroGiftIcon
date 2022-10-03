package com.zerogift.batch.infrastructure;

import com.zerogift.batch.infrastructure.dto.ResultListenerDto;
import com.zerogift.batch.infrastructure.dto.SlackRequestDto;
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
