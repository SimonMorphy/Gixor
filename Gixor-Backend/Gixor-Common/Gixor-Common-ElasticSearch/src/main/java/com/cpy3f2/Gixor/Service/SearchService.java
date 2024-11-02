package com.cpy3f2.Gixor.Service;

import co.elastic.clients.json.JsonData;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author : simon
 * @description : 响应式ES查询服务
 * @since : 2024-11-02 02:59
 */
@Slf4j
@Service
public class SearchService {

    @Resource
    private ReactiveElasticsearchOperations elasticsearchOperations;

    /**
     * 精确匹配查询
     * @param field 字段名
     * @param value 查询值
     * @param clazz 返回类型
     * @return Flux<T> 查询结果流
     */
    public <T> Flux<T> termQuery(String field, Object value, Class<T> clazz) {
        Query query = NativeQuery.builder()
                .withQuery(q -> q.term(t -> t.field(field).value(v -> v.stringValue(value.toString()))))
                .build();
        return elasticsearchOperations.search(query, clazz)
                .map(SearchHit::getContent);
    }

    /**
     * 模糊查询
     * @param field 字段名
     * @param keyword 关键词
     * @param clazz 返回类型
     * @return Flux<T> 查询结果流
     */
    public <T> Flux<T> fuzzyQuery(String field, String keyword, Class<T> clazz) {
        Query query = NativeQuery.builder()
                .withQuery(q -> q.fuzzy(f -> f.field(field).value(keyword)))
                .build();
        return elasticsearchOperations.search(query, clazz)
                .map(SearchHit::getContent);
    }

    /**
     * 范围查询
     * @param field 字段名
     * @param from 起始值
     * @param to 结束值
     * @param clazz 返回类型
     * @return Flux<T> 查询结果流
     */
    public <T> Flux<T> rangeQuery(String field, Object from, Object to, Class<T> clazz) {
        Query query = NativeQuery.builder()
                .withQuery(q -> q.range(r -> r
                        .field(field)
                        .gte(JsonData.of(from))
                        .lte(JsonData.of(to))))
                .build();
        return elasticsearchOperations.search(query, clazz)
                .map(SearchHit::getContent);
    }

    /**
     * 分页查询
     * @param page 页码
     * @param size 每页大小
     * @param clazz 返回类型
     * @return Flux<T> 查询结果流
     */
    public <T> Flux<T> pageQuery(int page, int size, Class<T> clazz) {
        Query query = NativeQuery.builder()
                .withPageable(PageRequest.of(page, size))
                .build();
        return elasticsearchOperations.search(query, clazz)
                .map(SearchHit::getContent);
    }

    /**
     * 复合查询
     * @param mustQueries 必须满足的查询条件
     * @param shouldQueries 可选满足的查询条件
     * @param clazz 返回类型
     * @return Flux<T> 查询结果流
     */
    public <T> Flux<T> boolQuery(List<Query> mustQueries, List<Query> shouldQueries, Class<T> clazz) {
        Query query = NativeQuery.builder()
                .withQuery(q -> q.bool(b -> {
                    if (mustQueries != null) {
                        mustQueries.forEach(mustQuery -> 
                            b.must(((NativeQuery)mustQuery).getQuery()));
                    }
                    if (shouldQueries != null) {
                        shouldQueries.forEach(shouldQuery -> 
                            b.should(((NativeQuery)shouldQuery).getQuery()));
                    }
                    return b;
                }))
                .build();
        return elasticsearchOperations.search(query, clazz)
                .map(SearchHit::getContent);
    }

    /**
     * 聚合查询
     * @param field 聚合字段
     * @param clazz 返回类型
     * @return Mono<Long> 聚合结果
     */
    public Mono<Long> count(String field, Class<?> clazz) {
        Query query = NativeQuery.builder()
                .withQuery(q -> q.exists(e -> e.field(field)))
                .build();
        return elasticsearchOperations.count(query, clazz);
    }

    /**
     * 批量保存
     * @param documents 文档列表
     * @return Mono<Long> 保存成功数量
     */
    public <T> Mono<Long> bulkSave(Flux<T> documents) {
        return documents.flatMap(doc -> 
            elasticsearchOperations.save(doc)
        ).count();
    }
}
