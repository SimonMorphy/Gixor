package com.cpy3f2.Gixor.Config;

import feign.Feign;
import feign.reactive.ReactorFeign;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancer;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-26 00:50
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-26 00:50
 */
@Configuration
public class FeignConfig {
    @Bean
    public HttpMessageConverters messageConverters() {
        return new HttpMessageConverters();
    }

    @Bean
    @Primary
    public ReactiveLoadBalancer.Factory<ServiceInstance> reactiveLoadBalancerFactory(
            LoadBalancerClientFactory loadBalancerClientFactory) {
        return loadBalancerClientFactory;
    }

    @Bean
    public Feign.Builder feignBuilder() {
        return ReactorFeign.builder()
                .contract(new SpringMvcContract())
                .decoder(new SpringDecoder(this::messageConverters))
                .encoder(new SpringEncoder(this::messageConverters));
    }
}
