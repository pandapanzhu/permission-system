

package com.shanyu.permission.modules.sys.controller;

import com.shanyu.common.controller.AbstractController;
import com.shanyu.common.utils.AESUtil;
import com.shanyu.common.utils.IPUtils;
import com.shanyu.permission.modules.sys.config.Constant;
import com.shanyu.permission.modules.sys.entity.dto.SysPermissionDTO;
import com.shanyu.permission.modules.sys.entity.po.SysLoginLog;
import com.shanyu.permission.modules.sys.entity.SysMenuEntity;
import com.shanyu.permission.modules.sys.entity.dto.UserInfoDTO;
import com.shanyu.permission.modules.sys.form.SysLoginForm;
import com.shanyu.permission.modules.sys.service.*;
import com.shanyu.common.sys.entity.dto.UserDTO;
import com.shanyu.common.utils.R;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 登录相关
 * @author pan
 */
@RestController
public class SysLoginController extends AbstractController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserTokenService sysUserTokenService;
    @Autowired
    private SysCaptchaService sysCaptchaService;

    @Resource
    private SysLoginLogService sysLoginLogService;


    /**
     * 验证码
     */
    @GetMapping("captcha.jpg")
    public R captcha(HttpServletResponse response, String uuid) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        //生成图片验证码
        String base64Img = sysCaptchaService.getCaptcha(uuid);
        return R.ok().put("data", "data:image/png;base64," + base64Img);
    }

    /**
     * 登录
     */
    @PostMapping("/sys/login")
    public R login(@RequestBody SysLoginForm form, HttpServletRequest request) throws IOException {
        boolean captcha = sysCaptchaService.validate(form.getUuid(), form.getCaptcha());
        if (!captcha) {
            return R.error(500, "验证码不正确");
        }

        //用户信息
        UserDTO user = sysUserService.queryByUserName(form.getUsername());
        if (ObjectUtils.isEmpty(user)) {
            return R.error(401, "账号或密码不正确");
        }

        //先解码，解码之后再加密
        String dePassword = AESUtil.decrypt(form.getPassword(), Constant.AES_KEY, 1);
        String encodeHashPassword = new Sha256Hash(dePassword, user.getSalt()).toHex();
        //账号不存在、密码错误
        if (user == null || !user.getPassword().equals(encodeHashPassword)) {
            return R.error(401, "账号或密码不正确");
        }

        //账号锁定
        if (user.getStatus() == 0) {
            return R.error(403, "账号已被锁定,请联系管理员");
        }
        //生成token，并保存到数据库
        Map<String, Object> map = sysUserTokenService.createToken(user);

        String ipAddress = IPUtils.getIpAddr(request);
        String userId = user.getUserId().toString();
        Integer loginTimes = 0;
        // 判断完用户密码之后，还要判断该用户是否是第一次在这个地址登录
        loginTimes = sysLoginLogService.selectLogByUserIdAndIp(userId, ipAddress);
        map.put("times", loginTimes);
        // 判断完成后还需要插入一条登录记录
        SysLoginLog log = SysLoginLog.builder()
                .userId(userId)
                .ip(ipAddress)
                .type("PC")
                .createTime(new Date())
                .build();
        sysLoginLogService.insertLoginLog(log);
        return R.ok().put("data", map);
    }


    /**
     * 退出
     */
    @PostMapping("/sys/logout")
    public R logout(HttpServletRequest request) {
        sysUserTokenService.logout(request.getHeader("token"));
        return R.ok();
    }

}
