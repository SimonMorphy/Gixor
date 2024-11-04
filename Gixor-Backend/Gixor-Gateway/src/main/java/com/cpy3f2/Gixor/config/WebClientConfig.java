package com.cpy3f2.Gixor.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerClientRequestTransformer;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.cloud.client.loadbalancer.reactive.RetryableLoadBalancerExchangeFilterFunction;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-03 20:25
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-03 20:25
 */
@Configuration
public class WebClientConfig {
    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder()
                .filter(ExchangeFilterFunction.ofResponseProcessor(
                        clientResponse -> {
                            if (clientResponse.statusCode().is5xxServerError()) {
                                return Mono.error(new RuntimeException("远程服务错误"));
                            }
                            return Mono.just(clientResponse);
                        }
                ));
    }
    @Bean
    public WebClient webClient(
            WebClient.Builder builder,
            ReactorLoadBalancerExchangeFilterFunction filterFunction) {
        return builder
                .filter(filterFunction)
                .build();
    }
    @Bean
    public ReactorLoadBalancerExchangeFilterFunction loadBalancerExchangeFilterFunction(
            LoadBalancerClientFactory loadBalancerClientFactory,
            ObjectProvider<List<LoadBalancerClientRequestTransformer>> transformers) {
        return new ReactorLoadBalancerExchangeFilterFunction(
                loadBalancerClientFactory,
                transformers.getIfAvailable(Collections::emptyList)
        );
    }
}
