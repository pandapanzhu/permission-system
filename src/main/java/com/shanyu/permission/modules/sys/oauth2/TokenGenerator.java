package com.shanyu.permission.modules.sys.oauth2;


import com.shanyu.common.exception.GlobalException;
import com.shanyu.common.sys.entity.dto.UserDTO;
import com.shanyu.common.utils.AESUtil;

import java.util.UUID;

/**
 * 生成token
 */
public class TokenGenerator {

    private static final String key = "chengdu3yukejief";

    public static String generateValue(UserDTO user) {
        try {
            return AESUtil.encrypt(user.getUserId()+UUID.randomUUID().toString(), key);
        } catch (Exception e) {
            throw new GlobalException("生成Token失败", e);
        }
    }




}
