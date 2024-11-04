package com.cpy3f2.Gixor.Task;

import com.cpy3f2.Gixor.Constant.Constants;
import com.cpy3f2.Gixor.Domain.GitHubUser;
import com.cpy3f2.Gixor.Domain.ResponseResult;
import com.cpy3f2.Gixor.Service.GitHubUserService;
import com.cpy3f2.Gixor.service.CacheService;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-04 19:49
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-04 19:49
 */

@RestController
public class RankTask {

    @Resource
    private GitHubUserService gitHubUserService;


    @Resource
    private CacheService cacheService;

    @Scheduled(cron = "0 0 */1 * * ?")
    @GetMapping("/inList")
    public void rank() {
        gitHubUserService.getRanking(0, 100)
                .collectList()
                .subscribe(list->cacheService
                        .setCacheList(Constants.RANK_KEY,list)
                        .subscribe());
    }
}
