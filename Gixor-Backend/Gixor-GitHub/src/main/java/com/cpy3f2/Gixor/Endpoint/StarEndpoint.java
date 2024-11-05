package com.cpy3f2.Gixor.Endpoint;

import com.cpy3f2.Gixor.Annotation.Endpoint;
import com.cpy3f2.Gixor.Domain.ResponseResult;
import com.cpy3f2.Gixor.Service.StarService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.service.annotation.PutExchange;
import reactor.core.publisher.Mono;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-30 18:25
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-30 18:25
 */
@Endpoint("/star")
public class StarEndpoint {


    @Resource
    private StarService starService;


    /**
     * 检查当前用户是否收藏了指定仓库
     * @param owner 指定仓库的拥有者
     * @param repo  指定仓库的名称
     * @return M
     * @author simon
     * @since 2024/10/31
     * */
    @GetMapping("/{owner}/{repo}")
    public Mono<ResponseResult> check(@PathVariable String owner, @PathVariable String repo){
        return starService.isStarred(owner, repo)
                .then(Mono.fromCallable(() -> ResponseResult.success("已收藏")));
    }


    /**
     * 为指定仓库Star
     * @param owner 指定仓库的拥有者
     * @param repo 指定仓库的名称
     * @return Mono<ResponseResult>
     * @author simon
     * @since 2024/10/31
     */
    @PutExchange("/{owner}/{repo}")
    public Mono<ResponseResult> star(@PathVariable String owner, @PathVariable String repo){
        return starService.star(owner, repo)
                .then(Mono.fromCallable(() -> ResponseResult.success("收藏成功")));
    }

    /**
     * 为指定仓库取消Star
     * @param owner 指定仓库的拥有者
     * @param repo  指定仓库的名称
     * @return Mono<ResponseResult>
     * @author simon
     * @since 2024/10/31
     */
    @DeleteMapping("/{owner}/{repo}")
    public Mono<ResponseResult> unStar(@PathVariable String owner, @PathVariable String repo){
        return starService.unStar(owner, repo)
                .then(Mono.fromCallable(() -> ResponseResult.success("取消收藏成功")));
    }


}
