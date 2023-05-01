package com.shanyu.permission.modules.sys.entity.dto;

import com.shanyu.permission.modules.sys.entity.SysRoleEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pan
 * 用户和角色关系返回的对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysUserRoleResponseDTO {

    private Long roleId;

    private String roleName;

    /**
     * 是否被选择
     */
    private Integer checked;

    public SysUserRoleResponseDTO(SysRoleEntity entity) {
        this.roleId = entity.getRoleId();
        this.roleName = entity.getRoleName();
        // 默认都是没选中的，为了方便创建
        this.checked = 0;
    }
}
