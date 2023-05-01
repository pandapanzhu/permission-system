

package com.shanyu.permission.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shanyu.permission.modules.sys.entity.SysUserRoleEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户与角色对应关系
 */
@Mapper
public interface SysUserRoleDao extends BaseMapper<SysUserRoleEntity> {

    /**
     * 根据用户ID，获取角色ID列表
     */
    @Select(" select role_id from sys_user_role where user_id = #{userId}")
    List<Long> queryRoleIdList(Long userId);


    /**
     * 根据角色ID数组，批量删除
     */
    @Delete("<script> delete from  sys_user_role where role_id in\n" +
            "        <foreach item=\"roleId\" collection=\"array\" open=\"(\" separator=\",\" close=\")\">\n" +
            "            #{roleId}\n" +
            "        </foreach>" +
            "</script>")
    int deleteBatch(Long[] roleIds);

    /**
     * 根据roleId查询用户和角色的关系
     *
     * @param roleId
     * @return
     */
    @Select("select count(1) from sys_user_role where role_id = #{roleId}")
    int selectCountByRoleId(Long roleId);
}
