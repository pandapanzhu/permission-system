package com.shanyu.permission.modules.sys.entity.dto;

import com.shanyu.permission.modules.sys.entity.SysRoleEntity;
import com.shanyu.permission.modules.sys.entity.po.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionDTO {

    private Long roleId;

    private String roleName;

    /**
     * 角色的状态
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    private Date createTime;

    private Date updateTime;

    /**
     * 角色对应的权限
     */
    private List<SysPermissionDTO> permissions;

    public RolePermissionDTO(SysRoleEntity entity) {
        this.roleId = entity.getRoleId();
        this.roleName = entity.getRoleName();
        this.status = entity.getStatus();
        this.remark = entity.getRemark();
        this.createTime = entity.getCreateTime();
        this.updateTime = entity.getUpdateTime();
    }
}
