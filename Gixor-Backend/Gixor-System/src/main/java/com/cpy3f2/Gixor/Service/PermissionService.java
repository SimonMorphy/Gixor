package com.cpy3f2.Gixor.Service;

import reactor.core.publisher.Flux;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-02 21:23
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-02 21:23
 */
public interface PermissionService {
    /**
     * 根据角色ID获取权限列表
     * @param id 角色ID
     * @return Flux<String>
     * @author simon
     * @since 2024/11/3
     */
    Flux<String> getPermissionList(Long id);
}
