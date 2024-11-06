package com.cpy3f2.Gixor.Endpoint;

import com.cpy3f2.Gixor.Annotation.Endpoint;
import com.cpy3f2.Gixor.Domain.Discussion;
import com.cpy3f2.Gixor.Domain.Query.DiscussionQuerySetting;
import com.cpy3f2.Gixor.Domain.ResponseResult;
import com.cpy3f2.Gixor.Service.DiscussionService;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Mono;

/**
 * @author : simon
 * @description : GitHub Discussions接口
 * @last : 2024-11-06 00:23
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-06 00:23
 */
@Endpoint("/disc")
public class DiscussionEndpoint {
    
    @Resource
    private DiscussionService discussionService;
    
    /**
     * 获取仓库的讨论列表
     * @param owner 仓库所有者
     * @param repo 仓库名称
     * @param settings 查询参数
     * @return Mono<ResponseResult>
     */
    @GetMapping(
        value = "/{owner}/{repo}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<ResponseResult> listDiscussions(@PathVariable String owner,
                                              @PathVariable String repo,
                                              DiscussionQuerySetting settings) {
        return discussionService.listDiscussions(owner, repo, settings)
                .map(ResponseResult::success);
    }
}
