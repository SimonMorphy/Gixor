package com.cpy3f2.Gixor.Endpoint;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.cpy3f2.Gixor.Annotation.Endpoint;
import com.cpy3f2.Gixor.Service.PermissionService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-03 13:42
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-03 13:42
 */
@Endpoint("/perm")
public class PermissionEndpoint {


    @Resource
    private PermissionService permissionService;

    @GetMapping("/{id}")
    public Flux<String> getPermissionList(@PathVariable Long id) {
        return permissionService.getPermissionList(id);
    }


}
