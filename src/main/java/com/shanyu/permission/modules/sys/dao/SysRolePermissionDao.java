package com.shanyu.permission.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shanyu.permission.modules.sys.entity.po.SysPermissionEntity;
import com.shanyu.permission.modules.sys.entity.po.SysRolePermissionEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Collection;
import java.util.List;

/**
 * @author pan
 * 角色与权限的对应关系
 */
@Mapper
public interface SysRolePermissionDao extends BaseMapper<SysRolePermissionEntity> {

    /**
     * 根据角色id，查询对应的权限
     *
     * @param roleIds
     * @return
     */
    @Select("<script>" +
            " select sp.permission_id as permissionId,sp.permission_name as permissionName, srp.actions as actions,srp.role_id as roleId, sp.actions as actionsPermission " +
            " from sys_permission sp join sys_role_permission srp on sp.permission_id = srp.permission_id where srp.status = 1 and sp.status = 1" +
            " <if test='roleIds!=null and roleIds.size()>0 '> " +
            " and srp.role_id in" +
            "        <foreach item=\"roleId\" collection=\"roleIds\" open=\"(\" separator=\",\" close=\")\">\n" +
            "            #{roleId}\n" +
            "        </foreach>" +
            " </if> " +
            "</script>")
    List<SysPermissionEntity> findRolePermissionsByRoleIds(@Param("roleIds") List<Long> roleIds);

    /**
     * 根据权限找关系
     *
     * @param permissionId
     * @return
     */
    @Select(" select count(1) from  sys_role_permission where permission_id = #{permissionId}")
    Long findByPermissionId(String permissionId);

    /**
     * 根据角色找关系
     *
     * @param roleId
     * @return
     */
    @Select(" select count(1) from  sys_role_permission where role_id = #{roleId}")
    Long findByRoleId(Long roleId);

    /**
     * 根据roleId删除关系
     *
     * @param roleId
     * @return
     */
    @Delete(" delete from sys_role_permission where role_id = #{roleId}")
    int deleteByRoleId(Long roleId);


    /**
     * 批量插入 仅适用于mysql
     *
     * @param entityList 实体列表
     * @return 影响行数
     */
    Integer insertBatchSomeColumn(Collection<SysRolePermissionEntity> entityList);
}

