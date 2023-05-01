package com.shanyu.permission.modules.sys.service;

import com.shanyu.common.dto.IDDTO;
import com.shanyu.common.utils.PageNoUtils;
import com.shanyu.permission.modules.sys.entity.dto.ActionEntrySet;
import com.shanyu.permission.modules.sys.entity.dto.SysPermissionDTO;
import com.shanyu.permission.modules.sys.entity.dto.SysPermissionRequestDTO;
import com.shanyu.permission.modules.sys.entity.po.SysPermissionEntity;

import java.util.List;

/**
 * @author pan
 */
public interface SysPermissionService {

    /**
     * 获取用户的权限
     *
     * @param userId
     */
    List<SysPermissionDTO> getUserPermissionById(Long userId);

    /**
     * 通过roleIdList获取permission的信息
     *
     * @param roleIdList
     * @return
     */
    List<SysPermissionDTO> getPermissionByRoleIdList(List<Long> roleIdList);

    /**
     * action转换
     *
     * @param list
     * @return
     */
    List<SysPermissionDTO> setPermissionAllAction(List<SysPermissionEntity> list, boolean isAll);

    /**
     * 获取分页的权限信息
     *
     * @param pageNo
     * @param pageSize
     * @param permissionName
     * @param status
     * @return
     */
    PageNoUtils getPermissionPage(int pageNo, int pageSize, String permissionName, String status,String sortField, String sortOrder);

    /**
     * 这里是拿所有的action
     *
     * @return
     */
    List<ActionEntrySet> loadAllPermissionAction();

    /**
     * 保存权限信息
     *
     * @param dto
     */
    void save(SysPermissionRequestDTO dto);

    /**
     * 禁用权限
     *
     * @param id
     */
    void freezePermission(IDDTO id);

    /**
     * 启用权限
     *
     * @param id
     */
    void unFreezePermission(IDDTO id);

    /**
     * 删除权限
     *
     * @param id
     */
    void deletePermission(IDDTO id);

    /**
     * 拿到配置好的permission
     * @return
     */
    List<SysPermissionDTO> getPermissionList();
}
