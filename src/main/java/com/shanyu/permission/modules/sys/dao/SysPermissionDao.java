package com.shanyu.permission.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shanyu.permission.modules.sys.entity.dto.ActionEntrySet;
import com.shanyu.permission.modules.sys.entity.po.SysPermissionEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author pan
 * 权限相关信息在这查
 */
@Mapper
public interface SysPermissionDao  extends BaseMapper<SysPermissionEntity> {
    /**
     * 加载所有的action
     * @return
     */
    @Select("select * from sys_permission_action ")
    List<ActionEntrySet> loadAllPermissionAction();

    /**
     * 根据permissionId查询相关信息
     * @param permissionId
     * @return
     */
    @Select(" select * from sys_permission where permission_id = #{permissionId} ")
    SysPermissionEntity selectByPermissionId(String permissionId);

    /**
     * 真删除操作
     * @param permissionId
     * @return
     */
    @Delete(" delete from  sys_permission where permission_id = #{permissionId}")
    int deleteByPermissionId(String permissionId);
}
