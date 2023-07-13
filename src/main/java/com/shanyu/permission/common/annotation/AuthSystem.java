package com.shanyu.permission.common.annotation;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启动权限相关的注解
 * 添加此注解可直接启动权限相关的校验
 * @author pan
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
//@EnableConfigurationProperties(Auth2Properties.class)
//@Import({Auth2Registrar.class, Auth2WebConfig.class})
public @interface AuthSystem {
}
