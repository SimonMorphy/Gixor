package com.cpy3f2.Gixor.Domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import jakarta.validation.constraints.*;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-26 00:33
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-26 00:33
 */


@Table("rel_role_permission")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolePermission {

    @Column("id")
    private Long id;

    @Column("role_id")
    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    @Column("permission_id")
    @NotNull(message = "权限ID不能为空")
    private Long permissionId;
}
