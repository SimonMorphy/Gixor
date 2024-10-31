package com.cpy3f2.Gixor.Controller;

import com.cpy3f2.Gixor.Domain.ResponseResult;
import com.cpy3f2.Gixor.Domain.User;
import com.cpy3f2.Gixor.Service.UserService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-26 01:07
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-26 01:07
 */
@RestController
@HttpExchange("/user")
@Validated
@Slf4j
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
