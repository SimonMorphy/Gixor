package com.cpy3f2.Gixor.Service;

import com.cpy3f2.Gixor.Config.GitHubApi;
import com.cpy3f2.Gixor.Domain.GitHubRepository;
import com.cpy3f2.Gixor.Domain.GitHubUser;
import com.cpy3f2.Gixor.Domain.Query.BaseQuerySetting;
import com.cpy3f2.Gixor.Domain.Query.SearchQuerySetting;
import com.cpy3f2.Gixor.Domain.SimpleUser;
import com.cpy3f2.Gixor.Domain.VO.RepositorySearchVO;
import com.cpy3f2.Gixor.Domain.VO.UserSearchVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-06 10:56
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-06 10:56
 */
@Service
public class SearchService {
    @Resource
    private WebClient githubClient;

    public Mono<RepositorySearchVO> searchRepo(SearchQuerySetting setting) {
        return githubClient.get()
                .uri(uriBuilder -> GitHubApi.addQueryParams(
                                uriBuilder.path("/search/repositories"),
                                setting)
                        .build())
                .retrieve()
                .bodyToMono(RepositorySearchVO.class);
    }
    public Mono<UserSearchVO> searchUser(SearchQuerySetting setting) {
        return githubClient.get()
                .uri(uriBuilder -> GitHubApi.addQueryParams(
                                uriBuilder.path("/search/users"),
                                setting)
                        .build())
                .retrieve()
                .bodyToMono(UserSearchVO.class);
    }
}
