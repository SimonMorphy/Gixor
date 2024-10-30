package com.cpy3f2.Gixor.Service.Impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import com.cpy3f2.Gixor.Domain.User;
import com.cpy3f2.Gixor.Repository.UserRepository;
import com.cpy3f2.Gixor.Service.UserService;
import jakarta.annotation.Resource;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-26 01:13
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-26 01:13
 */

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserRepository userRepository;

    @Resource
    private R2dbcEntityTemplate template;

    @Override
    public Mono<Boolean> add(User user) {
        return template.insert(user)
                .map(Objects::nonNull);
    }

    @Override
    public Mono<Boolean> exists(String uuid) {
        return userRepository.existsById(Long.parseLong(uuid));
    }
}
