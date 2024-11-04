package com.cpy3f2.Gixor.Endpoint;

import com.cpy3f2.Gixor.Annotation.Endpoint;
import com.cpy3f2.Gixor.Service.RoleService;
import com.cpy3f2.Gixor.Service.UserRoleService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Flux;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-03 13:42
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-03 13:42
 */
@Endpoint("/role")
public class RoleEndpoint {

    @Resource
    private RoleService roleService;

    @GetMapping("/{id}")
    public Flux<String> getRoleList(@PathVariable Long id) {
        return roleService.getRoleList(id);
    }

}
