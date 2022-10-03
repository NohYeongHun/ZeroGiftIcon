package com.zerogift.batch.infrastructure;

import com.zerogift.batch.infrastructure.dto.SlackRequestDto;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "SlackClient", url = "${slack.url}")
public interface SlackClient {

    @PostMapping("/services/{channel}")
    Response message(@PathVariable("channel") String channel,
        @RequestBody SlackRequestDto slackRequestDto);

}
