package com.shanyu.permission.modules.sys.entity.dto;

import com.shanyu.permission.modules.sys.entity.SysUserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author pan
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {
    private String username;
    private String email;
    private String mobile;
    private Integer status;
    /**
     * 菜单(权限)信息在这儿
     */
    private List<SysPermissionDTO> permissions;
    private Date createTime;

    /**
     * 用户的角色
     */
    private List<Long> roles;

    public UserInfoDTO(SysUserEntity entity) {
        this.username = entity.getUsername();
        this.email = entity.getEmail();
        this.mobile = entity.getMobile();
        this.status = entity.getStatus();
        this.createTime = entity.getCreateTime();
    }
}
