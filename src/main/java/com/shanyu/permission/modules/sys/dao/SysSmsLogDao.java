package com.shanyu.permission.modules.sys.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shanyu.permission.modules.sys.entity.SysSmsLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 发送短信日志Dao
 */
@Mapper
public interface SysSmsLogDao extends BaseMapper<SysSmsLogEntity> {

}
