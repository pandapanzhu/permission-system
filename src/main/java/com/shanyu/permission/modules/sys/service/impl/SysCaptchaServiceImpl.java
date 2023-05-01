

package com.shanyu.permission.modules.sys.service.impl;


import cn.hutool.core.codec.Base64;
import com.google.code.kaptcha.Producer;
import com.shanyu.common.exception.GlobalException;
import com.shanyu.permission.modules.sys.service.SysCaptchaService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 验证码相关
 * @author pan
 */
@Service("sysCaptchaService")
public class SysCaptchaServiceImpl  implements SysCaptchaService {
    @Autowired
    private Producer producer;
    @Resource
    private RedisTemplate redisTemplate;

    @Value("${captcha.uidPrex}")
    private String captchaUidPrefix;

    /**
     * 校验uid的合法性
     *
     * @param uuid
     * @return
     */
    public boolean validateCaptchaUUID(String uuid) {
        if (StringUtils.isBlank(uuid)) {
            throw new GlobalException("uuid不能为空");
        }
        String[] arr = uuid.split("_");
        String prefix = arr[0];
        if (prefix.startsWith(captchaUidPrefix)) {
            return true;
        }
        return false;
    }

    @Override
    public String getCaptcha(String uuid) throws IOException {
        if (!validateCaptchaUUID(uuid)) {
            throw new GlobalException("获取验证码失败，请联系管理员");
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            //生成文字验证码
            String code = producer.createText();

            //删除过期的验证码
            String[] arr = uuid.split("_");
            String prefix = arr[0];
            Set<String> keys = redisTemplate.keys(prefix.concat("*"));
            if (keys.size() > 0) {
                redisTemplate.delete(keys);
            }

            //保存新的验证码
            redisTemplate.opsForValue().set(uuid, code.toUpperCase(), 5, TimeUnit.MINUTES);

            BufferedImage image = producer.createImage(code);

            ImageIO.write(image, "jpg", stream);
            String base64 = Base64.encode(stream.toByteArray());
            return base64;

        } catch (Exception e) {
            throw new GlobalException("获取验证码失败" + e.getMessage());
        } finally {
            stream.flush();
            stream.close();
        }

    }

    @Override
    public boolean validate(String uuid, String code) {
        Object codeObject = redisTemplate.opsForValue().get(uuid);
        Long expire = redisTemplate.getExpire(uuid, TimeUnit.SECONDS);
        //删除验证码
        redisTemplate.delete(uuid);
        if (!ObjectUtils.isEmpty(codeObject) && codeObject.equals(code.toUpperCase()) && null != expire && expire > 0) {
            return true;
        }

        return false;
    }
}
