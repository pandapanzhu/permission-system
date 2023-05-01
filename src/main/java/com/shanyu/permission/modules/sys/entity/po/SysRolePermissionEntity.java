

package com.shanyu.permission.modules.sys.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 角色与权限的对应关系
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_role_permission")
public class SysRolePermissionEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 菜单ID
     */
    private String permissionId;

    /**
     * 这里保存的是权限的二级操作
     * 包括增删查改等
     * 数据格式如下：
     * [{"action":"add","defaultCheck":false,"describe":"新增"},{"action":"query","defaultCheck":false,"describe":"查询"}]
     * 这一个是从权限操作中选出来的
     */
    private String actions;

}
