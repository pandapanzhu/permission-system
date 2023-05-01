

package com.shanyu.permission.modules.sys.service;

import java.io.IOException;

/**
 * 验证码
 *
 * @author pan
 */
public interface SysCaptchaService {

    /**
     * 获取图片验证码
     */
    String getCaptcha(String uuid) throws IOException;

    /**
     * 验证码效验
     *
     * @param uuid uuid
     * @param code 验证码
     * @return true：成功  false：失败
     */
    boolean validate(String uuid, String code);
}
