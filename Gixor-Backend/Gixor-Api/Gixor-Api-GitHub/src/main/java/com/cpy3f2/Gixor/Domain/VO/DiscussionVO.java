package com.cpy3f2.Gixor.Domain.VO;

import com.cpy3f2.Gixor.Domain.Discussion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-06 01:52
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-06 01:52
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscussionVO {
    private List<Discussion> nodes;
    private PageInfo pageInfo;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageInfo {
        private boolean hasNextPage;
        private String endCursor;
    }
}
