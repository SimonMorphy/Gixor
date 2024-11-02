package com.cpy3f2.Gixor.Controller;

import com.cpy3f2.Gixor.Annotation.Endpoint;
import com.cpy3f2.Gixor.Domain.User;
import com.cpy3f2.Gixor.Service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-26 01:07
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-26 01:07
 */
@Slf4j
@Endpoint("/user")
public class UserController {


    @Resource
    private UserService userService;



    @PostMapping
    public Mono<Boolean> addUser(@RequestBody User user) {
        return userService.add(user);
    }

    @GetMapping("/exists/{uuid}")
    public Mono<Boolean> exists(@PathVariable String uuid) {
        return userService.exists(uuid);
    }

}

