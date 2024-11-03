package com.cpy3f2.Gixor.Domain;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
/**
 * @author : simon
 * @description :
 * @last : 2024-10-26 00:34
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-26 00:34
 */



@Table("tbl_auth")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Auth {

    @Column("id")
    private Long id;

    @Column("user_id")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Column("github_id")
    @NotBlank(message = "GitHub ID不能为空")
    private String githubId;

    @Column("github_access_token")
    @NotBlank(message = "GitHub访问令牌不能为空")
    private String githubAccessToken;

    @Column("token_update_time")
    private LocalDateTime tokenUpdateTime;

    @Column("create_time")
    @CreatedDate
    private LocalDateTime createTime;

    @Column("update_time")
    @LastModifiedDate
    private LocalDateTime updateTime;
}
