package com.cpy3f2.Gixor.Service;

import com.cpy3f2.Gixor.Domain.Discussion;
import com.cpy3f2.Gixor.Domain.Query.DiscussionQuerySetting;
import com.cpy3f2.Gixor.Domain.VO.DiscussionVO;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.cpy3f2.Gixor.Convertor.DiscussionConvertor.extractDiscussions;
import static com.cpy3f2.Gixor.Convertor.DiscussionConvertor.extractPageInfo;
import static com.cpy3f2.Gixor.Domain.Query.DiscussionQueryBuilder.buildDiscussionQuery;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-05 22:06
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-05 22:06
 */
@Service
@Slf4j
public class DiscussionService {

    @Resource
    private WebClient githubClient;

    public Mono<DiscussionVO> listDiscussions(String owner, String repo, DiscussionQuerySetting settings) {
        return githubClient.post()
                .uri("/graphql")
                .bodyValue(buildDiscussionQuery(owner, repo, settings))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(response -> {
                    JsonNode discussionsNode = response.path("data")
                            .path("repository")
                            .path("discussions");
                    return DiscussionVO.builder()
                            .nodes(extractDiscussions(discussionsNode.path("nodes")))
                            .pageInfo(extractPageInfo(discussionsNode.path("pageInfo")))
                            .build();
                });
    }

}