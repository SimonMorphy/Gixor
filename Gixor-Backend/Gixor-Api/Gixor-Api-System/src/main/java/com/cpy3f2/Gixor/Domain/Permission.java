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
 * @description : 权限实体类
 * @last : 2024-10-26 00:18
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-26 00:18
 */
@Table("tbl_permission")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
    @Id
    @Column("id")
    private Long id;

    @Column("permission_name")
    @NotBlank(message = "权限名称不能为空")
    @Size(max = 50, message = "权限名称长度不能超过50个字符")
    private String permissionName;

    @Column("permission_key")
    @NotBlank(message = "权限键不能为空")
    @Size(max = 100, message = "权限键长度不能超过100个字符")
    private String permissionKey;

    @Column("status")
    @NotNull(message = "状态不能为空")
    @Pattern(regexp = "[01]", message = "状态只能是0或1")
    private Character status;

    @Column("create_time")
    @CreatedDate
    private LocalDateTime createTime;

    @Column("update_time")
    @LastModifiedDate
    private LocalDateTime updateTime;
}
