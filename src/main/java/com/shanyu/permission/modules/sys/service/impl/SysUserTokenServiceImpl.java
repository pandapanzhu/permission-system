

package com.shanyu.permission.modules.sys.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.shanyu.common.sys.entity.dto.UserDTO;
import com.shanyu.common.utils.R;
import com.shanyu.common.utils.StringUtils;
import com.shanyu.permission.modules.sys.oauth2.TokenGenerator;
import com.shanyu.permission.modules.sys.service.SysUserTokenService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * 生成token相关权限
 * @author pan
 */
@Service("sysUserTokenService")
public class SysUserTokenServiceImpl implements SysUserTokenService {
    //2小时后过期
    private final static int EXPIRE = 3600 * 2;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public Map<String,Object> createToken(UserDTO user) {
        Map<String,Object> map = new HashMap<>(3);
        //生成一个token
        String token = TokenGenerator.generateValue(user);

        //判断是否生成过token
        redisTemplate.opsForValue().set(token, JSONObject.toJSONString(user), EXPIRE, TimeUnit.SECONDS);
        map.put("token", token);
        map.put("expire", EXPIRE);
        return map;
    }

    /**
     * @param token
     */
    @Override
    public void logout(String token) {
        if(StringUtils.isNotEmpty(token)){
            redisTemplate.delete(token);
        }
    }
}
