package com.cpy3f2.Gixor.Repository;

import com.cpy3f2.Gixor.Domain.RolePermission;
import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-02 21:36
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-02 21:36
 */
public interface RolePermissionRepository extends R2dbcRepository<RolePermission, Long> {
    /**
     * 根据角色ID查询角色权限列表
     * @param roleIds 角色ID集合
     * @return Flux<RolePermission>
     * @author simon
     * @since 2024/11/3
     */
    Flux<RolePermission> findAllByRoleIdIn(List<Long> roleIds);
}
