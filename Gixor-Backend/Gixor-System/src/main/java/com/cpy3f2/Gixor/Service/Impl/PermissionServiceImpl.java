package com.cpy3f2.Gixor.Service.Impl;

import com.cpy3f2.Gixor.Constant.Constants;
import com.cpy3f2.Gixor.Domain.Permission;
import com.cpy3f2.Gixor.Domain.RolePermission;
import com.cpy3f2.Gixor.Domain.UserRole;
import com.cpy3f2.Gixor.Repository.PermissionRepository;
import com.cpy3f2.Gixor.Repository.RolePermissionRepository;
import com.cpy3f2.Gixor.Repository.UserRoleRepository;
import com.cpy3f2.Gixor.Service.PermissionService;
import com.cpy3f2.Gixor.Service.UserRoleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-02 21:24
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-02 21:24
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Resource
    private RolePermissionRepository rolePermissionRepository;


    @Resource
    private UserRoleRepository userRoleRepository;


    @Resource
    private PermissionRepository permissionRepository;


    @Override
    public Flux<String> getPermissionList(Long id) {
        return userRoleRepository.findAllByUserId(id)
                .map(UserRole::getRoleId)
                .collectList()
                .flatMapMany(rolePermissionRepository::findAllByRoleIdIn)
                .map(RolePermission::getPermissionId)
                .collectList()
                .flatMapMany(permissionRepository::findAllByIdIn)
                .filter(permission -> Constants.EFFECTIVE.equals(permission.getStatus()))
                .map(Permission::getPermissionKey)
                .distinct()
                .sort();
    }
}
