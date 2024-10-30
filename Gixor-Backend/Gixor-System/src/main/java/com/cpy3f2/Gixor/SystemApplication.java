package com.cpy3f2.Gixor;

import com.cpy3f2.Gixor.Annotation.Application;
import com.cpy3f2.Gixor.Annotation.RemoteDiscovery;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-25 17:05
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-25 17:05
 */
@Application
@RemoteDiscovery
public class SystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class,args);
    }
}
