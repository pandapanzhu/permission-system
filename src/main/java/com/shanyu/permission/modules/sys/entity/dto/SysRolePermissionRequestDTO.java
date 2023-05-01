package com.shanyu.permission.modules.sys.entity.dto;

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
public class SysRolePermissionRequestDTO {

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

    /**
     * 权限下辖的操作有哪些
     */
    private List<RolePermissionSelectDTO> permissions;

}
