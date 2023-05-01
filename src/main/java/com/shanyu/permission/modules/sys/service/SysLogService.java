

package com.shanyu.permission.modules.sys.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.shanyu.common.utils.PageUtils;
import com.shanyu.permission.modules.sys.entity.po.SysLogEntity;

import java.util.Map;


/**
 * 系统日志
 */
public interface SysLogService extends IService<SysLogEntity> {

    PageUtils queryPage(Map<String, Object> params);

}
