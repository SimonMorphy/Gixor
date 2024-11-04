package com.cpy3f2.Gixor.Repository;

import com.cpy3f2.Gixor.Domain.Permission;
import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-02 21:25
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-02 21:25
 */
@Repository
public interface PermissionRepository extends R2dbcRepository<Permission, Long> {
    /**
     * 根据角色ID列表查询权限列表
     * @param roleIds 角色ID列表
     * @return Flux<Permission>
     * @author simon
     * @since 2024/11/3
     */
    Flux<Permission> findAllByIdIn(List<Long> roleIds);
}
