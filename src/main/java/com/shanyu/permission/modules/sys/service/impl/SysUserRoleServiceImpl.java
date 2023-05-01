

package com.shanyu.permission.modules.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shanyu.common.utils.MapUtils;
import com.shanyu.permission.modules.sys.dao.SysUserRoleDao;
import com.shanyu.permission.modules.sys.entity.SysUserRoleEntity;
import com.shanyu.permission.modules.sys.service.SysUserRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * 用户与角色对应关系
 *
 * @author pan
 */
@Service("sysUserRoleService")
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleDao, SysUserRoleEntity> implements SysUserRoleService {

    @Resource
    private SysUserRoleDao sysUserRoleDao;

    @Override
    public void saveOrUpdate(Long userId, List<String> roleIdList) {
        //先删除用户与角色关系
        this.removeByMap(new MapUtils().put("user_id", userId));

        if (roleIdList == null || roleIdList.size() == 0) {
            return;
        }

        //保存用户与角色关系
        for (String roleId : roleIdList) {
            Long roleLong = Long.parseLong(roleId);
            SysUserRoleEntity sysUserRoleEntity = new SysUserRoleEntity();
            sysUserRoleEntity.setUserId(userId);
            sysUserRoleEntity.setRoleId(roleLong);
            this.save(sysUserRoleEntity);
        }
    }

    @Override
    public List<Long> queryRoleIdList(Long userId) {
        return baseMapper.queryRoleIdList(userId);
    }

    @Override
    public int deleteBatch(Long[] roleIds) {
        return baseMapper.deleteBatch(roleIds);
    }

    /**
     * 根据roleId 查询用户与角色的关系
     *
     * @param roleId
     * @return
     */
    @Override
    public int selectReleationByRoleId(Long roleId) {
        return sysUserRoleDao.selectCountByRoleId(roleId);
    }

    /**
     * 通过用户名查用户的角色
     *
     * @param username
     * @return
     */
    @Override
    public List<Long> queryRoleIdListByUserName(String username) {
        return null;
    }
}
