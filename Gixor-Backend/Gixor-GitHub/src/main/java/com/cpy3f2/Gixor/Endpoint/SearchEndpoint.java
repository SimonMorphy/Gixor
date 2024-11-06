package com.cpy3f2.Gixor.Endpoint;

import com.cpy3f2.Gixor.Annotation.Endpoint;
import com.cpy3f2.Gixor.Domain.Query.SearchQuerySetting;
import com.cpy3f2.Gixor.Domain.ResponseResult;
import com.cpy3f2.Gixor.Service.SearchService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-06 10:59
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-06 10:59
 */
@Endpoint("/search")
public class SearchEndpoint {
    @Resource
    private SearchService searchService;

    @GetMapping("/repo")
    public Mono<ResponseResult> searchRepo(SearchQuerySetting setting) {
        return searchService.searchRepo(setting)
                .map(ResponseResult::success);
    }

    @GetMapping("/user")
    public Mono<ResponseResult> searchUser(SearchQuerySetting setting) {
        return searchService.searchUser(setting)
                .map(ResponseResult::success);
    }
}
