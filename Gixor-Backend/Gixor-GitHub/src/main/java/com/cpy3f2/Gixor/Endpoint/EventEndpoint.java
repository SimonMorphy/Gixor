package com.cpy3f2.Gixor.Endpoint;

import com.cpy3f2.Gixor.Annotation.Endpoint;
import com.cpy3f2.Gixor.Domain.Query.EventQuerySetting;
import com.cpy3f2.Gixor.Domain.ResponseResult;
import com.cpy3f2.Gixor.Service.EventService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

/**
 * @author : simon
 * @description : GitHub Events相关接口
 * @since : 2024-11-05
 */
@Endpoint("/event")
public class EventEndpoint {

    @Resource
    private EventService eventService;

    /**
     * 获取公开事件列表
     */
    @GetMapping("/public")
    public Mono<ResponseResult> listPublicEvents(EventQuerySetting settings) {
        return eventService.listPublicEvents(settings)
                .collectList()
                .map(ResponseResult::success);
    }

    /**
     * 获取用户接收到的事件
     */
    @GetMapping("/users/{username}/received")
    public Mono<ResponseResult> listUserReceivedEvents(
            @PathVariable String username,
            EventQuerySetting settings) {
        return eventService.listUserReceivedEvents(username, settings)
                .collectList()
                .map(ResponseResult::success);
    }

    /**
     * 获取用户的事件列表
     */
    @GetMapping("/users/{username}")
    public Mono<ResponseResult> listUserEvents(
            @PathVariable String username,
            EventQuerySetting settings) {
        return eventService.listUserEvents(username, settings)
                .collectList()
                .map(ResponseResult::success);
    }

    /**
     * 获取组织的事件列表
     */
    @GetMapping("/orgs/{org}")
    public Mono<ResponseResult> listOrganizationEvents(
            @PathVariable String org,
            EventQuerySetting settings) {
        return eventService.listOrganizationEvents(org, settings)
                .collectList()
                .map(ResponseResult::success);
    }
}
