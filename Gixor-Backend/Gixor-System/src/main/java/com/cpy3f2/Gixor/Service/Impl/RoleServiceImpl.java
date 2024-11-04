package com.cpy3f2.Gixor.Service.Impl;

import com.cpy3f2.Gixor.Domain.Role;
import com.cpy3f2.Gixor.Domain.UserRole;
import com.cpy3f2.Gixor.Repository.RoleRepository;
import com.cpy3f2.Gixor.Repository.UserRoleRepository;
import com.cpy3f2.Gixor.Service.RoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-02 21:23
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-02 21:23
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private UserRoleRepository userRoleRepository;


    @Resource
    private RoleRepository roleRepository;

    @Override
    public Flux<String> getRoleList(Long id) {
        return userRoleRepository.findAllByUserId(id)
                .map(UserRole::getRoleId)
                .collectList()
                .flatMapMany(roleIds -> roleRepository.findAllByIdIn(roleIds))
                .map(Role::getKey)
                .distinct();
    }
}
