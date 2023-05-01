

package com.shanyu.permission.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shanyu.permission.modules.sys.entity.SysMenuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 菜单管理
 */
@Mapper
public interface SysMenuDao extends BaseMapper<SysMenuEntity> {

    /**
     * 根据父菜单，查询子菜
     *
     * @param parentId 父菜单ID
     */
    @Select(" select * from  sys_menu where parent_id = #{parentId} order by order_num asc")
    List<SysMenuEntity> queryListParentId(Long parentId);

    /**
     * 获取不包含按钮的菜单列表
     */
    @Select("select * from  sys_menu where type != 2 order by order_num asc")
    List<SysMenuEntity> queryNotButtonList();

}
