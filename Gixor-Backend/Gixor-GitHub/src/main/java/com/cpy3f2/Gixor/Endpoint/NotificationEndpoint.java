package com.cpy3f2.Gixor.Endpoint;

import com.cpy3f2.Gixor.Annotation.Endpoint;
import com.cpy3f2.Gixor.Domain.Query.NotificationQuerySetting;
import com.cpy3f2.Gixor.Domain.ResponseResult;
import com.cpy3f2.Gixor.Service.NotificationService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PutExchange;
import reactor.core.publisher.Mono;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-30 18:34
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-30 18:34
 */
@Endpoint("/noti")
@Slf4j
public class NotificationEndpoint {


    @Resource
    private NotificationService notificationService;

    @GetExchange
    public Mono<ResponseResult> getNotification(NotificationQuerySetting settings){
        return notificationService
                .getNotification(settings)
                .collectList()
                .map(ResponseResult::success);
    }
    @PutExchange
    public Mono<ResponseResult> markAsRead(){
        return notificationService
                .markAsRead()
                .then(Mono.just(ResponseResult.success("已读成功！")));
    }
}
