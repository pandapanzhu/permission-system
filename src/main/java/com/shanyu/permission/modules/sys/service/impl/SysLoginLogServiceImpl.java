package com.shanyu.permission.modules.sys.service.impl;

import com.shanyu.permission.modules.sys.dao.SysLoginLogDao;
import com.shanyu.permission.modules.sys.entity.po.SysLoginLog;
import com.shanyu.permission.modules.sys.service.SysLoginLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author pan
 */
@Service
public class SysLoginLogServiceImpl implements SysLoginLogService {

    @Resource
    private SysLoginLogDao sysLoginLogDao;

    @Override
    public int insertLoginLog(SysLoginLog log) {
        return sysLoginLogDao.insert(log);
    }

    @Override
    public Integer selectLogByUserIdAndIp(String userId, String ip) {
        return sysLoginLogDao.selectCountByUserIdAndIp(userId,ip);
    }
}
