package com.shanyu.permission.modules.sys.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class TokenUtils {

    /**
     * 获取请求的token
     */
    public static String getRequestToken() {
        HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        //从header中获取token
        String token = httpRequest.getHeader("token");

        //如果header中不存在token，则从参数中获取token
        if (StringUtils.isBlank(token)) {
            token = httpRequest.getParameter("token");
            if (StringUtils.isBlank(token)) {
                token = httpRequest.getHeader("Access-Token");
            }
        }
        return token;
    }

}
