package com.cpy3f2.Gixor;

import com.cpy3f2.Gixor.Annotation.Application;
import com.cpy3f2.Gixor.Annotation.RemoteDiscovery;
import org.springframework.boot.SpringApplication;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-30 18:05
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-30 18:05
 */
@Application
@RemoteDiscovery
public class GitHubApplication {
    public static void main(String[] args) {
        SpringApplication.run(GitHubApplication.class, args);
    }
}