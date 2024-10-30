package com.cpy3f2.Gixor.Domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-26 00:18
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-26 00:18
 */
@Table("tbl_role")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @Column("id")
    @NotBlank(message = "角色ID不能为空")
    private Long id;

    @Column("role_name")
    @NotBlank(message = "角色名称不能为空")
    @Size(max = 50, message = "角色名称长度不能超过50个字符")
    private String name;

    @Column("role_key")
    @NotBlank(message = "角色键不能为空")
    @Size(max = 100, message = "角色键长度不能超过100个字符")
    private String key;

    @Column("status")
    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态值不能小于0")
    @Max(value = 1, message = "状态值不能大于1")
    private Integer status;

    @Column("create_time")
    @CreatedDate
    private LocalDateTime createTime;

    @Column("update_time")
    @LastModifiedDate
    private LocalDateTime updateTime;

}
