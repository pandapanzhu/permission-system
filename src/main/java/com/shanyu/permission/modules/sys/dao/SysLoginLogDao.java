package com.shanyu.permission.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shanyu.permission.modules.sys.entity.po.SysLoginLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author pan
 */
@Mapper
public interface SysLoginLogDao extends BaseMapper<SysLoginLog> {

    @Select(" select count(1) from sys_login_log where user_id = #{userId} and ip = #{ip} and  DATE_SUB(CURDATE(), INTERVAL 30 DAY) <= date(create_time);" )
    Integer selectCountByUserIdAndIp(@Param("userId") String userId, @Param("ip") String ip);
}
