package com.cpy3f2.Gixor.Domain.Query;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * @author : simon
 * @description :
 * @last : 2024-11-04 11:11
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-11-04 11:11
 */
@Data
@SuperBuilder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class ActivityQuerySetting extends BaseQuerySetting {
    private Boolean all;
    private Boolean participating;
    private LocalDateTime since;
    private LocalDateTime before;
}
