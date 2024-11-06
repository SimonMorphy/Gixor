package com.cpy3f2.Gixor.Domain.Query;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-05 22:01
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-05 22:01
 */
@Slf4j
public class DiscussionQueryBuilder {

    private static final String DISCUSSION_QUERY_TEMPLATE = """
        query {
          repository(owner: "%s", name: "%s") {
            discussions(%s) {
              pageInfo {
                hasNextPage
                endCursor
              }
              nodes {
                title
                body
                createdAt
                author {
                  login
                }
                category {
                  name
                }
                comments(first: %d) {
                  nodes {
                    body
                    author {
                      login
                    }
                  }
                }
              }
            }
          }
        }
    """;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 构建Discussion查询参数
     */
    public static String buildDiscussionParams(DiscussionQuerySetting settings) {
        List<String> params = new ArrayList<>();

        // first参数
        if (settings.getFirst() != null) {
            params.add("first: " + settings.getFirst());
        }

        // after参数（游标）
        if (StringUtils.hasText(settings.getAfter())) {
            params.add("after: \"" + settings.getAfter() + "\"");
        }

        // orderBy参数
        if (StringUtils.hasText(settings.getOrderBy())) {
            String direction = StringUtils.hasText(settings.getDirection()) ? 
                    settings.getDirection() : "DESC";
            params.add("orderBy: {field: " + settings.getOrderBy() + ", direction: " + direction + "}");
        }

        return String.join(", ", params);
    }

    /**
     * 生成完整的GraphQL查询语句
     */
    public static String buildDiscussionQuery(String owner, String repo, DiscussionQuerySetting settings) {
        try {
            // 构建查询参数
            String discussionParams = buildDiscussionParams(settings);
            int commentFirst = settings.getFirst() != null ? settings.getFirst() : 10;
            
            // 构建完整查询
            String query = String.format(DISCUSSION_QUERY_TEMPLATE,
                    owner,
                    repo,
                    discussionParams,
                    commentFirst);
            
            // 构建请求体
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("query", query);


            return objectMapper.writeValueAsString(requestBody);
        } catch (Exception e) {
            throw new RuntimeException("查询失败", e);
        }
    }
}