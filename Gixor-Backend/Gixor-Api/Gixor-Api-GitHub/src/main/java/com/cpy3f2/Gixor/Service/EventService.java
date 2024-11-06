package com.cpy3f2.Gixor.Service;

import com.cpy3f2.Gixor.Config.GitHubApi;
import com.cpy3f2.Gixor.Domain.Event;
import com.cpy3f2.Gixor.Domain.Query.EventQuerySetting;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *  事件服务
 * @author simon
 * @since 2024/11/5 */

@Service
public class EventService {
    
    @Resource
    private WebClient githubClient;
    @Resource
    private WebClient gitHubClientWithoutToken;

    /**
     * 获取公开事件列表
     */
    public Flux<Event> listPublicEvents(EventQuerySetting settings) {
        return gitHubClientWithoutToken.get()
                .uri(uriBuilder -> GitHubApi.addQueryParams(
                        uriBuilder.path("/events"),
                        settings)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        response -> Mono.error(new RuntimeException("获取事件列表失败")))
                .bodyToFlux(Event.class);
    }

    /**
     * 获取用户接收到的事件
     */
    public Flux<Event> listUserReceivedEvents(String username, EventQuerySetting settings) {
        return githubClient.get()
                .uri(uriBuilder -> GitHubApi.addQueryParams(
                        uriBuilder.path("/users/{username}/received_events"),
                        settings)
                        .build(username))
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        response -> Mono.error(new RuntimeException("获取用户接收事件失败")))
                .bodyToFlux(Event.class);
    }

    /**
     * 获取用户的事件列表
     */
    public Flux<Event> listUserEvents(String username, EventQuerySetting settings) {
        return githubClient.get()
                .uri(uriBuilder -> GitHubApi.addQueryParams(
                        uriBuilder.path("/users/{username}/events"),
                        settings)
                        .build(username))
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        response -> Mono.error(new RuntimeException("获取用户事件失败")))
                .bodyToFlux(Event.class);
    }

    /**
     * 获取组织的事件列表
     */
    public Flux<Event> listOrganizationEvents(String org, EventQuerySetting settings) {
        return githubClient.get()
                .uri(uriBuilder -> GitHubApi.addQueryParams(
                        uriBuilder.path("/orgs/{org}/events"),
                        settings)
                        .build(org))
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        response -> Mono.error(new RuntimeException("获取组织事件失败")))
                .bodyToFlux(Event.class);
    }
} 