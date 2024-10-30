package com.cpy3f2.Gixor.Service;

import com.cpy3f2.Gixor.Domain.User;
import reactor.core.publisher.Mono;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-26 01:08
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-26 01:08
 */
public interface UserService {
    /**
     *  添加用户
     * @param user
     * @return Mono<Boolean>
     * @author simon
     * @since 2024/10/26
     */
    Mono<Boolean> add(User user);

    /**
     *
     * @param uuid
     * @return Mono<Boolean>
     * @author simon
     * @since 2024/10/28
     */

    Mono<Boolean> exists(String uuid);
}
