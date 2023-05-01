

package com.shanyu.permission.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shanyu.permission.modules.sys.entity.SysRoleMenuEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 角色与菜单对应关系
 */
@Mapper
public interface SysRoleMenuDao extends BaseMapper<SysRoleMenuEntity> {

    /**
     * 根据角色ID，获取菜单ID列表
     */
    @Select(" select menu_id from  sys_role_menu where role_id = #{value}")
    List<Long> queryMenuIdList(Long roleId);

    /**
     * 根据角色ID数组，批量删除
     */
    @Delete("<script>" +
            "delete from  sys_role_menu where role_id in\n" +
            "        <foreach item=\"roleId\" collection=\"array\" open=\"(\" separator=\",\" close=\")\">\n" +
            "            #{roleId}\n" +
            "        </foreach>" +
            "</script>")
    int deleteBatch(Long[] roleIds);
}
