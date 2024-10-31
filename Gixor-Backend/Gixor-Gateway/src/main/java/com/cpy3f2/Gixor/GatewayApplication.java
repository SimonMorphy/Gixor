package com.cpy3f2.Gixor;

import com.cpy3f2.Gixor.Annotation.Application;
import com.cpy3f2.Gixor.Annotation.RemoteDiscovery;
import org.springframework.boot.SpringApplication;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-27 16:45
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-27 16:45
 */
@Application
@RemoteDiscovery
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
