package com.shanyu.permission.common.aspect;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.shanyu.common.sys.entity.dto.UserDTO;
import com.shanyu.permission.modules.sys.util.TokenUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class SaveAspect implements MetaObjectHandler {

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("created_at", new Date(), metaObject);
        this.setFieldValByName("createdAt", new Date(), metaObject);

        this.setFieldValByName("create_time", new Date(), metaObject);
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("created_time", new Date(), metaObject);
        this.setFieldValByName("createdTime", new Date(), metaObject);
        String userName = getUserInfo();
        this.setFieldValByName("create_by", userName, metaObject);
        this.setFieldValByName("createBy", userName, metaObject);
        this.setFieldValByName("created_by", userName, metaObject);
        this.setFieldValByName("createdBy", userName, metaObject);

        this.updateFill(metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updated_at", new Date(), metaObject);
        this.setFieldValByName("updatedAt", new Date(), metaObject);
        this.setFieldValByName("update_time", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
        this.setFieldValByName("updated_time", new Date(), metaObject);
        this.setFieldValByName("updatedTime", new Date(), metaObject);
        String userName = getUserInfo();
        this.setFieldValByName("update_by", userName, metaObject);
        this.setFieldValByName("updateBy", userName, metaObject);
        this.setFieldValByName("updated_by", userName, metaObject);
        this.setFieldValByName("updatedBy", userName, metaObject);
    }

    /**
     * 获取请求的token
     */
    private String getUserInfo() {
        HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //从header中获取token
        String token = TokenUtils.getRequestToken();
        //如果header中不存在token，则从参数中获取token
        if (StringUtils.isNotBlank(token)) {
            Object userObject = redisTemplate.opsForValue().get(token);
            //token失效
            if (ObjectUtils.isEmpty(userObject)) {
                return null;
            }
            //查询用户信息
            UserDTO userDTO = JSONObject.parseObject(userObject.toString(), UserDTO.class);
            return userDTO.getUsername();
        } else {
            return null;
        }
    }

}