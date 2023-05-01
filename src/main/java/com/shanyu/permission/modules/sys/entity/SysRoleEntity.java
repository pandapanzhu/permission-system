

package com.shanyu.permission.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.shanyu.permission.modules.sys.entity.po.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 角色
 */
@Data
@TableName("sys_role")
public class SysRoleEntity  extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    @TableField(value = "role_id")
    private Long roleId;

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    /**
     * 创建者ID
     */
    private Long createUserId;

    @TableField(exist = false)
    private List<Long> menuIdList;



}
