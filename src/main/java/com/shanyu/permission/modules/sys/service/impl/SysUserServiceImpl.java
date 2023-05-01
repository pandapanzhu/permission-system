

package com.shanyu.permission.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shanyu.common.dto.IDDTO;
import com.shanyu.common.exception.GlobalException;
import com.shanyu.common.utils.PageNoUtils;
import com.shanyu.permission.modules.sys.config.Constant;
import com.shanyu.permission.modules.sys.dao.SysUserDao;
import com.shanyu.permission.modules.sys.entity.SysUserEntity;
import com.shanyu.permission.modules.sys.entity.dto.SysUserRoleRequestDTO;
import com.shanyu.permission.modules.sys.entity.dto.UserInfoDTO;
import com.shanyu.permission.modules.sys.service.SysRoleService;
import com.shanyu.permission.modules.sys.service.SysUserRoleService;
import com.shanyu.permission.modules.sys.service.SysUserService;
import com.shanyu.common.sys.entity.dto.UserDTO;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 系统用户
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {
    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Override
    public PageNoUtils queryPage(int pageNo, int pageSize, String sortField, String sortOrder, String name, String status) {
        PageNoUtils<SysUserEntity> pageNoUtils = new PageNoUtils<>();
        Page<SysUserEntity> page = pageNoUtils.initPageForMP(pageNo, pageSize, sortOrder, sortField);
        QueryWrapper<SysUserEntity> wrapper = pageNoUtils.initQueryWrapperForNameAndStatus("username", name, "status", status);
        this.page(page, wrapper);
        //人与角色的关系，等到查询详情的时候查
        List<UserInfoDTO> dtoList = page.getRecords().stream().map(record -> {
            UserInfoDTO dto = new UserInfoDTO(record);
            dto.setRoles(sysUserRoleService.queryRoleIdList(record.getUserId()));
            return dto;
        }).collect(Collectors.toList());
        return new PageNoUtils<>(dtoList, page.getTotal(), page.getSize(), page.getCurrent());
    }

    @Override
    public UserDTO queryByUserName(String username) {
        SysUserEntity entity = baseMapper.queryByUserName(username);
        if (ObjectUtils.isEmpty(entity)) {
            return null;
        }
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(SysUserRoleRequestDTO dto, Long userId) {
        SysUserEntity user = new SysUserEntity(dto, userId);
        // 查一遍
        SysUserEntity oldUser = baseMapper.queryByUserName(dto.getUsername());
        if (null == oldUser) {
            //sha256加密
            //加盐，生成20为随机数
            String salt = RandomStringUtils.randomAlphanumeric(20);
            user.setSalt(salt);
            //通过盐对用户密码进行hash加密
            user.setPassword(new Sha256Hash(dto.getMobile(), salt).toHex());
            user.setCreateUserId(userId);
            this.save(user);
        } else {
            user.setUserId(oldUser.getUserId());
            this.updateById(user);
        }
        //保存用户与角色关系
        sysUserRoleService.saveOrUpdate(user.getUserId(), dto.getRoles());
    }


    @Override
    public boolean updatePassword(Long userId, String password, String newPassword) {
        SysUserEntity sysUserEntity = baseMapper.selectById(userId);
        if (null == sysUserEntity) {
            throw new GlobalException(Constant.FIND_EMPTY);
        }
        //sha256加密
        //加盐，生成20为随机数
        String salt = sysUserEntity.getSalt();
        //通过盐对用户密码进行hash加密
        sysUserEntity.setPassword(new Sha256Hash(newPassword, salt).toHex());
        return this.update(sysUserEntity,
                new QueryWrapper<SysUserEntity>().eq("user_id", userId).eq("password", password));
    }

    @Override
    public int delete(IDDTO iddto) {
        SysUserEntity sysUserEntity = baseMapper.queryByUserName(iddto.getId());
        if (null == sysUserEntity) {
            throw new GlobalException(Constant.FIND_EMPTY);
        }
        //先检查用户是否绑定了角色
        List<Long> roleIdList = sysUserRoleService.queryRoleIdList(sysUserEntity.getUserId());
        if (roleIdList.size() > 0) {
            throw new GlobalException(Constant.USER_ROLE_FIND);
        }
        //删除用户
        return baseMapper.delete(sysUserEntity.getUserId());
    }

    @Override
    public int freeze(IDDTO iddto) {
        return baseMapper.updateStatus(iddto.getId(), Constant.FREEZE_STATUS);
    }

    @Override
    public int unFreeze(IDDTO iddto) {
        return baseMapper.updateStatus(iddto.getId(), Constant.NORMAL_STATUS);
    }
}