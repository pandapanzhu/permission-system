package com.shanyu.permission.modules.sys.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author pan
 * 保存用户的对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysUserRoleRequestDTO {

    private Long userId;

    private String username;
    private String email;
    /**
     * 角色的状态
     */
    private Integer status;

    private String mobile;

    /**
     * 权限下辖的操作有哪些
     */
    private List<String> roles;

}
