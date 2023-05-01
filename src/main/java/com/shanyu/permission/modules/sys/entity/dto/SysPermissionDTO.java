package com.shanyu.permission.modules.sys.entity.dto;

import com.shanyu.permission.modules.sys.entity.po.SysPermissionEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author pan
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysPermissionDTO {

    private Long roleId;

    /**
     * 权限ID，对应菜单的路由
     */
    private String permissionId;

    /**
     * 权限名称，对应菜单路由的描述
     */
    private String permissionName;

    /**
     * 角色对应的权限的已选择的action
     */
    private String actions;

    private Integer status;

    private String describe;

    private String actionsPermission;

    /**
     * 这个是
     */
    private List<ActionEntrySet> actionEntitySet;

    public SysPermissionDTO(SysPermissionEntity entity) {
        this.roleId = entity.getRoleId();
        this.permissionId = entity.getPermissionId();
        this.permissionName = entity.getPermissionName();
        this.actions = entity.getActions();
        this.status = entity.getStatus();
        this.describe = entity.getRemark();
        this.actionsPermission = entity.getActionsPermission();
    }

}
