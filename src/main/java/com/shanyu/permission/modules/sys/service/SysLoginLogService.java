package com.shanyu.permission.modules.sys.service;

import com.shanyu.permission.modules.sys.entity.po.SysLoginLog;

/**
 * 用户登录记录表
 * @author pan
 */
public interface SysLoginLogService {

    /**
     * 插入一条登录记录
     * @param log
     * @return
     */
    int insertLoginLog(SysLoginLog log);


    /**
     * 通过用户的ID和IP查该用户30天内是否有该地址的登录记录
     * @param userId
     * @param ip
     * @return
     */
    Integer selectLogByUserIdAndIp(String userId,String ip);
}
