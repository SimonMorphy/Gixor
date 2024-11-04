package com.cpy3f2.Gixor.Repository;

import com.cpy3f2.Gixor.Domain.Auth;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-02 21:25
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-02 21:25
 */
@Repository
public interface AuthRepository extends R2dbcRepository<Auth,Long> {
}
