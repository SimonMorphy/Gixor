package com.cpy3f2.Gixor.Repository;

import com.cpy3f2.Gixor.Domain.Role;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-02 21:22
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-02 21:22
 */
@Repository
public interface RoleRepository extends R2dbcRepository<Role, Long> {
    /**
     * 根据id列表查询角色列表
     * @param ids
     * @return Flux<Role>
     * @author simon
     * @since 2024/11/3
     */
    Flux<Role> findAllByIdIn(List<Long> ids);
}
