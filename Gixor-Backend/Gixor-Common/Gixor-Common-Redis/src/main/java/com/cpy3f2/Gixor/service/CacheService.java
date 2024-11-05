package com.cpy3f2.Gixor.service;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-25 11:04
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-25 11:04
 */
@Service
public class CacheService {
    @Resource
    private ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    public <T> Mono<Boolean> setCacheObject(final String key, final T value) {
        return reactiveRedisTemplate.opsForValue().set(key, value);
    }

    public <T> Mono<Boolean> setCacheObject(final String key, final T value, final Long timeout, final TimeUnit timeUnit) {
        return reactiveRedisTemplate.opsForValue().set(key, value, Duration.of(timeout, timeUnit.toChronoUnit()));
    }

    public Mono<Boolean> expire(final String key, final long timeout) {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    public Mono<Boolean> expire(final String key, final long timeout, final TimeUnit unit) {
        return reactiveRedisTemplate.expire(key, Duration.of(timeout, unit.toChronoUnit()));
    }

    public Mono<Long> getExpire(final String key) {
        return reactiveRedisTemplate.getExpire(key).map(Duration::getSeconds);
    }

    public Mono<Boolean> hasKey(String key) {
        return reactiveRedisTemplate.hasKey(key);
    }

    public <T>  Mono<T> getCacheObject(final String key,final Class<T> clazz) {
        return reactiveRedisTemplate.opsForValue().get(key)
                .map(obj -> JSON.parseObject(JSON.toJSONString(obj), clazz));
    }
    public <T> Flux<T> getCacheObjectFlux(final String key, final Class<T> clazz) {
        return reactiveRedisTemplate.opsForValue().get(key)
                .map(obj -> JSON.parseArray(JSON.toJSONString(obj), clazz))
                .flatMapMany(Flux::fromIterable);
    }

    public Mono<Long> deleteObject(final String key) {
        return reactiveRedisTemplate.delete(key);
    }

    public Mono<Long> deleteObject(final Collection<String> collection) {
        return reactiveRedisTemplate.delete(Flux.fromIterable(collection));
    }

    public <T> Mono<Long> setCacheList(final String key, final List<T> dataList) {
        return reactiveRedisTemplate.opsForList().rightPushAll(key, dataList.toArray());
    }

    public <T> Flux<T> getCacheList(final String key,final Class<T> clazz) {
        return reactiveRedisTemplate.opsForList().range(key, 0, -1)
                .map(obj -> JSON.parseObject(JSON.toJSONString(obj), clazz));
    }
    public <T> Flux<T> getCacheList(final String key,final Class<T> clazz,final int start, final int end) {
        return reactiveRedisTemplate.opsForList().range(key, start, end)
                .map(obj -> JSON.parseObject(JSON.toJSONString(obj), clazz));
    }

    public <T> Mono<Long> setCacheSet(final String key, final Set<T> dataSet) {
        return reactiveRedisTemplate.opsForSet().add(key, dataSet.toArray());
    }

    public <T> Flux<T> getCacheSet(final String key,final Class<T> clazz) {
        return reactiveRedisTemplate.opsForSet().members(key)
                .map(obj -> JSON.parseObject(JSON.toJSONString(obj), clazz));
    }

    public <T> Mono<Boolean> setCacheMap(final String key, final Map<String, T> dataMap) {
        return reactiveRedisTemplate.opsForHash().putAll(key, dataMap);
    }

    public Flux<Map> getCacheMap(final String key) {
        return reactiveRedisTemplate.opsForHash().entries(key).cast(Map.class);
    }

    public <T> Mono<Boolean> setCacheMapValue(final String key, final String hKey, final T value) {
        return reactiveRedisTemplate.opsForHash().put(key, hKey, value);
    }

    public <T> Mono<T> getCacheMapValue(final String key, final String hKey,final Class<T> clazz) {
        return reactiveRedisTemplate.opsForHash().get(key, hKey)
                .map(obj -> JSON.parseObject(JSON.toJSONString(obj), clazz));
    }

    public <T> Flux<T> getMultiCacheMapValue(final String key, final Collection<Object> hKeys,final Class<T> clazz) {
        return reactiveRedisTemplate.opsForHash().multiGet(key, hKeys).flatMapMany(Flux::fromIterable)
                .map(obj -> JSON.parseObject(JSON.toJSONString(obj), clazz));
    }

    public Mono<Long> deleteCacheMapValue(final String key, final String hKey) {
        return reactiveRedisTemplate.opsForHash().remove(key, hKey);
    }

    public Flux<String> keys(final String pattern) {
        return reactiveRedisTemplate.keys(pattern);
    }


}
