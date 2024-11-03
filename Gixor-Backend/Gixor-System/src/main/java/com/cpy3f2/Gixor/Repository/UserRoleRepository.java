package com.cpy3f2.Gixor.Repository;

import com.cpy3f2.Gixor.Domain.UserRole;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-02 21:26
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-02 21:26
 */
public interface UserRoleRepository extends R2dbcRepository<UserRole,Long> {
    /**
     * 根据用户ID查询角色列表
     * @param id 用户ID
     * @return Flux<UserRole>
     * @author simon
     * @since 2024/11/3
     */
    Flux<UserRole> findAllByUserId(Long id);
}
