

package com.shanyu.permission.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shanyu.common.dto.IDDTO;
import com.shanyu.common.utils.PageNoUtils;
import com.shanyu.permission.modules.sys.entity.SysRoleEntity;
import com.shanyu.permission.modules.sys.entity.dto.SysRolePermissionRequestDTO;
import com.shanyu.permission.modules.sys.entity.dto.SysUserRoleResponseDTO;

import java.util.List;


/**
 * 角色管理相关接口
 * @author pan
 */
public interface SysRoleService extends IService<SysRoleEntity> {

    void saveRole(SysRolePermissionRequestDTO dto);


    /**
     * 查询用户创建的角色ID列表
     */
    List<Long> queryRoleIdList(Long createUserId);

    PageNoUtils queryPage(int pageNo, int pageSize, String name, String status, String sortField, String sortOrder);

    /**
     * 删除角色
     * @param roleId
     */
    void deleteRole(IDDTO roleId);

    /**
     * 冻结
     * @param roleId
     */
    void freezeRole(IDDTO roleId);

    /**
     * 解冻
     * @param roleId
     */
    void unFreezeRole(IDDTO roleId);

    List<SysUserRoleResponseDTO> roleList();
}
