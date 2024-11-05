package com.cpy3f2.Gixor.Endpoint;

import com.cpy3f2.Gixor.Annotation.Endpoint;
import com.cpy3f2.Gixor.Domain.DTO.ForkDTO;
import com.cpy3f2.Gixor.Domain.Query.BaseQuerySetting;
import com.cpy3f2.Gixor.Domain.ResponseResult;
import com.cpy3f2.Gixor.Service.ForkService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Mono;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-05 19:43
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-05 19:43
 */
@Endpoint("/fork")
public class ForkEndpoint {

    @Resource
    private ForkService forkService;

    @PostMapping ("/{owner}/{repo}")
    public Mono<ResponseResult> fork(@PathVariable String owner,
                                     @PathVariable String repo,
                                     @RequestBody ForkDTO fork) {
        return forkService.fork(owner, repo, fork)
                .map(ResponseResult::success);
    }

    @GetMapping("/{owner}/{repo}")
    public Mono<ResponseResult> listForks(@PathVariable String owner,
                                          @PathVariable String repo,
                                          BaseQuerySetting setting) {
        return forkService.listForks(owner, repo, setting)
                .collectList()
                .map(ResponseResult::success);
    }

}
