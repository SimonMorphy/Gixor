package com.cpy3f2.Gixor.Repository;

import com.cpy3f2.Gixor.Domain.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-26 01:09
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-26 01:09
 */

@Repository
public interface UserRepository extends R2dbcRepository<User, Long> {

}
