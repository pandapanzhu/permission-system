

package com.shanyu.permission.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shanyu.permission.modules.sys.entity.SysRoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 角色管理
 */
@Mapper
public interface SysRoleDao extends BaseMapper<SysRoleEntity> {

    /**
     * 查询用户创建的角色ID列表
     */
    @Select(" select role_id from  sys_role where create_user_id = #{createUserId}")
    List<Long> queryRoleIdList(Long createUserId);

    @Update(" update sys_role set status = 0 where role_id = #{roleId}")
    int freezeRole(Long roleId);

    @Update(" update sys_role set status = 1 where role_id = #{roleId}")
    int unFreezeRole(Long roleId);

    /**
     *
     * @param roleId
     * @return
     */
    @Select(" select * from sys_role where role_id = #{roleId}")
    SysRoleEntity selectByRoleId(Long roleId);
}
