

package com.shanyu.permission.modules.sys.service;

import com.shanyu.common.sys.entity.dto.UserDTO;
import com.shanyu.common.utils.R;

import java.util.Map;

/**
 * 用户Token
 */
public interface SysUserTokenService {

    /**
     * 生成token
     *
     * @param user 用户信息
     */
    Map<String,Object> createToken(UserDTO user);

    /**
     * 退出，修改token值
     *
     * @param token
     */
    void logout(String token);

}
