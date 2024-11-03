package com.cpy3f2.Gixor.Service;

import reactor.core.publisher.Flux;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-02 21:23
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-02 21:23
 */
public interface RoleService {
    /**
     * 获取用户角色列表
     * @param id 用户ID
     * @return Flux<Role>
     * @author simon
     * @since 2024/11/3
     */
    Flux<String> getRoleList(Long id);
}
