package com.cpy3f2.Gixor.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import java.util.Set;

@Configuration
public class SentinelConfig {
    
    @Value("${spring.cloud.sentinel.datasource.ds.nacos.server-addr}")
    private String serverAddr;
    
    @Value("${spring.cloud.sentinel.datasource.ds.nacos.dataId}")
    private String dataId;
    
    @Value("${spring.cloud.sentinel.datasource.ds.nacos.groupId}")
    private String groupId;
    
    @PostConstruct
    public void init() {
        ReadableDataSource<String, Set<GatewayFlowRule>> flowRuleDataSource = new NacosDataSource<>(
                serverAddr,
                groupId,
                dataId,
                source -> JSON.parseObject(source, new TypeReference<>() {
                })
        );
        GatewayRuleManager.register2Property(flowRuleDataSource.getProperty());
    }
} 