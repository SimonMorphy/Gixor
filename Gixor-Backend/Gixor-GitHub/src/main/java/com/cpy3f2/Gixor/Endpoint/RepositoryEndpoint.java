package com.cpy3f2.Gixor.Endpoint;

import com.cpy3f2.Gixor.Annotation.Endpoint;
import com.cpy3f2.Gixor.Constant.Constants;
import com.cpy3f2.Gixor.Domain.Query.RepositoryQuerySetting;
import com.cpy3f2.Gixor.Domain.ResponseResult;
import com.cpy3f2.Gixor.Domain.TrendyRepository;
import com.cpy3f2.Gixor.Service.RepositoryService;
import com.cpy3f2.Gixor.service.CacheService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-31 17:19
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-31 17:19
 */
@Slf4j
@Endpoint("/repo")
public class RepositoryEndpoint {


    @Resource
    private RepositoryService repositoryService;




    @GetMapping("/{owner}/{repo}")
    public Mono<ResponseResult> getRepository(@PathVariable String owner, @PathVariable String repo){
        return repositoryService.getRepo(owner,repo)
                .map(ResponseResult::success);
    }
    /**
     * 获取仓库列表
     * @param settings 查询参数
     * @return Mono<ResponseResult>
     * @author simon
     * @since 2024/11/1
     */
    @GetMapping
    public Mono<ResponseResult> listRepositories(RepositoryQuerySetting settings){
        return repositoryService.listRepos(settings)
                .collectList()
                .map(ResponseResult::success);
    }

    @GetMapping("/{username}")
    public Mono<ResponseResult> listRepositories(@PathVariable String username, RepositoryQuerySetting settings){
        return repositoryService.listRepos(username,settings)
                .collectList()
                .map(ResponseResult::success);
    }




    /**
     * 获取当前用户收藏的仓库
     * @param settings 查询参数
     * @return Mono<ResponseResult>
     * @author simon
     * @since 2024/10/31
     */
    @GetMapping("/starred")
    public Mono<ResponseResult> listStarredRepositories(RepositoryQuerySetting settings){
        return repositoryService.listStarredRepo(settings)
                .collectList()
                .map(ResponseResult::success);
    }
    /**
     * 获取指定用户收藏的仓库
     * @param settings 查询参数
     * @return Mono<ResponseResult>
     * @author simon
     * @since 2024/10/31
     */
    @GetMapping("/starred/{username}")
    public Mono<ResponseResult> listStarredRepositories(@PathVariable String username, RepositoryQuerySetting settings) {
        return repositoryService.listStarredRepo(username,settings)
                .collectList()
                .map(ResponseResult::success);
    }



}
