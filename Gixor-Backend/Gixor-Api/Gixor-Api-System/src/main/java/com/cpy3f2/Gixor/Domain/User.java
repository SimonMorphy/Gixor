package com.cpy3f2.Gixor.Domain;

import jakarta.validation.constraints.*;
import jdk.jfr.Description;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * @author : simon
 * @description :
 * @last : 2024-10-25 21:22
 * Copyright (c) 2024. 保留所有权利。
 * @since : 2024-10-25 21:22
 */
@Table("tbl_user")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column("id")
    @NotBlank(message = "ID不能为空")
    public Long id;

    @Column("username")
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3到50个字符之间")
    public String username;

    @Column("nickname")
    @Size(max = 50, message = "昵称长度不能超过50个字符")
    public String nickname;

    @Column("password")
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, message = "密码长度不能少于6个字符")
    public String password;

    @Column("email")
    @Email(message = "邮箱格式不正确")
    public String email;

    @Column("phone")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    public String phone;

    @Column("status")
    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态值不能小于0")
    @Max(value = 1, message = "状态值不能大于1")
    public Integer status;

    @Column("avatar")
    @Max(value = 200, message = "头像地址长度不能超过200个字符")
    public String avatar;

    @Column("create_time")
    @CreatedDate
    public LocalDateTime createTime;

    @Column("update_time")
    @LastModifiedDate
    public LocalDateTime updateTime;

}
