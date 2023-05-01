package com.shanyu.permission.modules.sys.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.shanyu.permission.modules.sys.entity.dto.SysPermissionDTO;
import com.shanyu.permission.modules.sys.entity.dto.SysPermissionRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 权限菜单
 *
 * @author pan
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_permission")
public class SysPermissionEntity extends BaseEntity implements Serializable {

    @TableId
    private Long id;

    /**
     * 权限ID，对应菜单的路由
     */
    private String permissionId;

    /**
     * 权限名称，对应菜单路由的描述
     */
    private String permissionName;

    /**
     * 权限下辖的操作有哪些
     * [{"action":"add","defaultCheck":false,"describe":"新增"},{"action":"query","defaultCheck":false,"describe":"查询"}]
     */
    private String actions;

    /**
     * 这个权限能够拥有的action
     */
    @TableField(exist = false)
    private String actionsPermission;

    @TableField(exist = false)
    private Long roleId;

    public SysPermissionEntity(SysPermissionDTO dto) {
        this.permissionId = dto.getPermissionId();
        this.permissionName = dto.getPermissionName();
        this.actions = dto.getActions();
        this.status = dto.getStatus();
        this.remark = dto.getDescribe();
    }

    /**
     * action要单独算
     *
     * @param dto
     */
    public SysPermissionEntity(SysPermissionRequestDTO dto) {
        this.permissionId = dto.getPermissionId();
        this.permissionName = dto.getPermissionName();
        this.actions = dto.getActionString();
        this.status = dto.getStatus();
        this.remark = dto.getDescribe();
    }


}
