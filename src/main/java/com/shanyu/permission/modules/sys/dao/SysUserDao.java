

package com.shanyu.permission.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shanyu.permission.modules.sys.entity.SysUserEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 系统用户
 */
@Mapper
public interface SysUserDao extends BaseMapper<SysUserEntity> {

    /**
     * 查询用户的所有权限
     *
     * @param userId 用户ID
     */
    @Select("select m.perms from  sys_user_role ur\n" +
            "        LEFT JOIN  sys_role_menu rm on ur.role_id = rm.role_id\n" +
            "        LEFT JOIN  sys_menu m on rm.menu_id = m.menu_id\n" +
            "        where ur.user_id = #{userId}")
    List<String> queryAllPerms(Long userId);

    /**
     * 查询用户的所有菜单ID
     */
    @Select("select distinct rm.menu_id from  sys_user_role ur\n" +
            "        LEFT JOIN  sys_role_menu rm on ur.role_id = rm.role_id\n" +
            "        where ur.user_id = #{userId}")
    List<Long> queryAllMenuId(Long userId);

    /**
     * 根据用户名，查询系统用户
     */
    @Select("select * from  sys_user where username = #{username}")
    SysUserEntity queryByUserName(String username);

    /**
     * 删除用户
     *
     * @param userId
     * @return
     */
    @Delete(" delete from sys_user where user_id = #{userId} ")
    int delete(Long userId);

    @Update(" update sys_user set status = #{status} where username = #{username}")
    int updateStatus(@Param("username") String username, @Param("status") int status);

}
