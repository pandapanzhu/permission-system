

package com.shanyu.permission.modules.sys.service.impl;

import com.shanyu.permission.modules.sys.dao.SysMenuDao;
import com.shanyu.permission.modules.sys.dao.SysUserDao;
import com.shanyu.permission.modules.sys.entity.SysMenuEntity;
import com.shanyu.permission.modules.sys.service.ShiroService;
import com.shanyu.common.utils.Constant;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ShiroServiceImpl implements ShiroService {
    @Resource
    private SysMenuDao sysMenuDao;
    @Resource
    private SysUserDao sysUserDao;

    @Override
    public Set<String> getUserPermissions(long userId) {
        List<String> permsList;

        //系统管理员，拥有最高权限
        if (userId == Constant.SUPER_ADMIN) {
            List<SysMenuEntity> menuList = sysMenuDao.selectList(null);
            permsList = new ArrayList<>(menuList.size());
            for (SysMenuEntity menu : menuList) {
                permsList.add(menu.getPerms());
            }
        } else {
            permsList = sysUserDao.queryAllPerms(userId);
        }
        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for (String perms : permsList) {
            if (StringUtils.isBlank(perms)) {
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }
        return permsSet;
    }


}