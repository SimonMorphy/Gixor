package com.cpy3f2.Gixor.Config;

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchConfiguration;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchClient;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions;
import org.springframework.data.elasticsearch.support.HttpHeaders;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-01 19:44
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-01 19:44
 */
@Configuration
public class ElasticSearchConfig extends ReactiveElasticsearchConfiguration {

    @Value("${spring.elasticsearch.uris:39.105.98.60:9200}")
    private String elasticsearchUri;

    @Value("${spring.elasticsearch.connection-timeout:30s}")
    private String connectionTimeout;

    @Value("${spring.elasticsearch.socket-timeout:30s}")
    private String socketTimeout;

    @Value("${spring.elasticsearch.username:}")
    private String username;

    @Value("${spring.elasticsearch.password:}")
    private String password;

    @Override
    @NotNull
    public ClientConfiguration clientConfiguration() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/vnd.elasticsearch+json;compatible-with=8");
        headers.add("Content-Type", "application/json");

        var builder = ClientConfiguration.builder()
                .connectedTo(elasticsearchUri.split(","))
                .withConnectTimeout(Duration.parse("PT" + connectionTimeout))
                .withSocketTimeout(Duration.parse("PT" + socketTimeout))
                .withDefaultHeaders(headers);

        if (!username.isEmpty() && !password.isEmpty()) {
            builder.withBasicAuth(username, password);
        }

        return builder.build();
    }
    @Override
    @Bean
    public ElasticsearchCustomConversions elasticsearchCustomConversions() {
        return new ElasticsearchCustomConversions(
                List.of(new StringToFloatConverter(),new StringToLongConverter())
        );
    }

    @Bean
    @Override
    public ReactiveElasticsearchOperations reactiveElasticsearchOperations(
            @NotNull ElasticsearchConverter elasticsearchConverter,
            @NotNull ReactiveElasticsearchClient reactiveElasticsearchClient) {
        return super.reactiveElasticsearchOperations(elasticsearchConverter, reactiveElasticsearchClient);
    }
}
