

package com.shanyu.permission.modules.sys.oauth2;

import com.alibaba.fastjson.JSONObject;
import com.shanyu.permission.modules.sys.service.ShiroService;
import com.shanyu.common.sys.entity.dto.UserDTO;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Set;

/**
 * 认证
 */
@Component
public class OAuth2Realm extends AuthorizingRealm {
    @Autowired
    private ShiroService shiroService;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OAuth2Token;
    }

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        UserDTO user = (UserDTO) principals.getPrimaryPrincipal();
        Long userId = user.getUserId();

        //用户权限列表,
        Set<String> permsSet = shiroService.getUserPermissions(userId);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;
    }

    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken accessToken) throws AuthenticationException {
        String token = (String) accessToken.getPrincipal();
        //根据accessToken，查询用户信息
        Object userObject = redisTemplate.opsForValue().get(token);
        //token失效
        if (ObjectUtils.isEmpty(userObject)) {
            throw new IncorrectCredentialsException("token失效，请重新登录");
        }

        //查询用户信息
        UserDTO userDTO = JSONObject.parseObject(userObject.toString(), UserDTO.class);
        //账号锁定
        if (userDTO.getStatus() == 0) {
            throw new LockedAccountException("账号已被锁定,请联系管理员");
        }

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(userDTO,token, getName());
        return info;
    }
}
