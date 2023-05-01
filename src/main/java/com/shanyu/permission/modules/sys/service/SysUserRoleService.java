

package com.shanyu.permission.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shanyu.permission.modules.sys.entity.SysUserRoleEntity;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 用户与角色对应关系
 * @author pan
 */
public interface SysUserRoleService extends IService<SysUserRoleEntity> {

    /**
     * 保存用户与角色的关系
     * @param userId
     * @param roleIdList
     */
    void saveOrUpdate(Long userId, List<String> roleIdList);

    /**
     * 根据用户ID，获取角色ID列表
     */
    List<Long> queryRoleIdList(Long userId);

    /**
     * 根据角色ID数组，批量删除
     */
    int deleteBatch(Long[] roleIds);


    /**
     * 根据roleId 查询用户与角色的关系
     * @param roleId
     * @return
     */
    int selectReleationByRoleId(Long roleId);

    /**
     * 通过用户名查用户的角色
     * @param username
     * @return
     */
    List<Long> queryRoleIdListByUserName(String username);
}
