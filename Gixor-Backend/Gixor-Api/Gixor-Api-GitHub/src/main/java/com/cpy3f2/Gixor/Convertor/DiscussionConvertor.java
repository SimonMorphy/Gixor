package com.cpy3f2.Gixor.Convertor;

import com.cpy3f2.Gixor.Domain.Discussion;
import com.cpy3f2.Gixor.Domain.VO.DiscussionVO;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-06 02:51
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-06 02:51
 */
public class DiscussionConvertor {

    public static List<Discussion> extractDiscussions(JsonNode nodesNode) {
        return StreamSupport.stream(nodesNode.spliterator(), false)
                .map(DiscussionConvertor::convertToDiscussion)
                .collect(Collectors.toList());
    }

    public static DiscussionVO.PageInfo extractPageInfo(JsonNode pageInfoNode) {
        return DiscussionVO.PageInfo.builder()
                .hasNextPage(pageInfoNode.path("hasNextPage").asBoolean())
                .endCursor(pageInfoNode.path("endCursor").asText())
                .build();
    }

    /**
     * 将JsonNode转换为Discussion对象
     */
    public static Discussion convertToDiscussion(JsonNode node) {
        return Discussion.builder()
                .title(node.path("title").asText())
                .body(node.path("body").asText())
                .createdAt(LocalDateTime.parse(
                        node.path("createdAt").asText(),
                        DateTimeFormatter.ISO_DATE_TIME
                ))
                .authorName(node.path("author").path("login").asText())
                .categoryName(node.path("category").path("name").asText())
                .comments(convertComments(node.path("comments").path("nodes")))
                .build();
    }

    /**
     * 转换评论列表
     */
    public static List<Discussion.Comment> convertComments(JsonNode commentsNode) {
        List<Discussion.Comment> comments = new ArrayList<>();
        for (JsonNode commentNode : commentsNode) {
            comments.add(Discussion.Comment.builder()
                    .body(commentNode.path("body").asText())
                    .authorName(commentNode.path("author").path("login").asText())
                    .build());
        }
        return comments;
    }
}
