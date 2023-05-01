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
public class SysPermissionRequestDTO {

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
     */
    private List<String> actions;

    private String actionString;
    private Integer status;

    private String describe;

}
