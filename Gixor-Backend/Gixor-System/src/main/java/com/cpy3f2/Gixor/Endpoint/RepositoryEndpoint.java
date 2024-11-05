package com.cpy3f2.Gixor.Endpoint;

import com.cpy3f2.Gixor.Annotation.Endpoint;
import com.cpy3f2.Gixor.Constant.Constants;
import com.cpy3f2.Gixor.Domain.ResponseResult;
import com.cpy3f2.Gixor.Domain.TrendyRepository;
import com.cpy3f2.Gixor.Service.RepositoryService;
import com.cpy3f2.Gixor.service.CacheService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-04 15:14
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-04 15:14
 */
@Endpoint("/repo")
public class RepositoryEndpoint {


    @Resource
    private RepositoryService repositoryService;

    @Resource
    private CacheService cacheService;


    @GetMapping("/trendy")
    public Mono<ResponseResult> listTrendyRepositories(){
        return cacheService.getCacheObjectFlux(Constants.TRENDY_REPO_KEY, TrendyRepository.class)
                .collectList()
                .switchIfEmpty(repositoryService.listTrendyRepos().collectList())
                .map(ResponseResult::success);
    }
}
