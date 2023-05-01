package com.shanyu.permission.modules.sys.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author pan
 * 用户登录的日志信息
 * 主要记录了IP和登录方式
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName("sys_login_log")
public class SysLoginLog {

    @TableId
    private Long id;

    /**
     * 用户的ID
     */
    private String userId;

    /**
     * 登录的IP
     */
    private String ip;

    /**
     * 登录的方式
     */
    private String type;

    /**
     * 登录事件
     */
    private Date createTime;
}
