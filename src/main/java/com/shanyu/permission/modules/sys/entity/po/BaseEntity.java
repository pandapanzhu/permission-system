package com.shanyu.permission.modules.sys.entity.po;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;


@Data
public class BaseEntity {

    /**
     * 状态
     */
    @TableField(value = "status")
    Integer status;
    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    Date createTime;
    /**
     *
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    Date updateTime;
    /**
     * 创建者
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    String createBy;
    /**
     * 更新人
     */
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    String updateBy;
    /**
     * 备用字段
     */
    String remark;
}
