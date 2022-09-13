package com.zerogift.batch.application.client;

import com.zerogift.batch.application.client.dto.SlackRequestDto;
import feign.Response;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Qualifier("slackClient")
@FeignClient(name = "slack", url = "${slack.url}")
public interface SlackClient {

    @PostMapping("/services/{channel}")
    Response message(@PathVariable String channel,
                     @RequestBody SlackRequestDto slackRequestDto);

}
