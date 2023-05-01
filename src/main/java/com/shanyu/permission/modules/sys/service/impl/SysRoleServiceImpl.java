

package com.shanyu.permission.modules.sys.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shanyu.common.dto.IDDTO;
import com.shanyu.common.exception.GlobalException;
import com.shanyu.common.utils.PageNoUtils;
import com.shanyu.common.utils.StringUtils;
import com.shanyu.permission.modules.sys.config.Constant;
import com.shanyu.permission.modules.sys.dao.SysPermissionDao;
import com.shanyu.permission.modules.sys.dao.SysRoleDao;
import com.shanyu.permission.modules.sys.dao.SysRolePermissionDao;
import com.shanyu.permission.modules.sys.entity.SysRoleEntity;
import com.shanyu.permission.modules.sys.entity.dto.*;
import com.shanyu.permission.modules.sys.entity.po.SysPermissionEntity;
import com.shanyu.permission.modules.sys.entity.po.SysRolePermissionEntity;
import com.shanyu.permission.modules.sys.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 角色
 *
 * @author pan
 */
@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRoleEntity> implements SysRoleService {
    @Autowired
    private SysRoleMenuService sysRoleMenuService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Resource
    private SysPermissionService sysPermissionService;
    @Resource
    private SysRolePermissionDao sysRolePermissionDao;

    @Resource
    private SysPermissionDao sysPermissionDao;

    @Resource
    private SysRoleDao sysRoleDao;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRole(SysRolePermissionRequestDTO dto) {
        if (ObjectUtils.isEmpty(dto)) {
            throw new GlobalException(Constant.PARAM_ERROR);
        }
        Long roleId = dto.getRoleId();
        SysRoleEntity roleEntity = new SysRoleEntity();
        BeanUtils.copyProperties(dto, roleEntity);
        // 然后添加role_permission的关系
        List<RolePermissionSelectDTO> permissions = dto.getPermissions();
        //拿到所有的permission
        List<SysPermissionEntity> allPermissions = sysPermissionDao.selectList(null);
        //主要是校验name是否存在和action是否合法
        Map<String, String> allPermissionMap = allPermissions.stream().collect(Collectors.toMap(SysPermissionEntity::getPermissionId, SysPermissionEntity::getActions, (o, n) -> n));
        List<SysRolePermissionEntity> rolePermissionEntityList = new ArrayList<>();
        if (!ObjectUtils.isEmpty(roleId)) {
            sysRoleDao.updateById(roleEntity);
        } else {
            //生成roleId
            sysRoleDao.insert(roleEntity);
        }

        //遍历所有的permission
        for (RolePermissionSelectDTO userPermission : permissions) {
            String permissionId = userPermission.getName();
            List<String> actionList = userPermission.getActions();
            if (!CollectionUtils.isEmpty(actionList) && allPermissionMap.containsKey(permissionId)) {
                String permissionAction = allPermissionMap.get(permissionId);
                for (String action : actionList) {
                    if (!permissionAction.contains(action)) {
                        throw new GlobalException(Constant.PARAM_ERROR);
                    }
                }
                SysRolePermissionEntity sysRolePermission = new SysRolePermissionEntity();
                sysRolePermission.setRoleId(roleEntity.getRoleId());
                sysRolePermission.setPermissionId(permissionId);
                String actionString = String.join(",", actionList);
                sysRolePermission.setActions(actionString);
                sysRolePermission.setStatus(1);
                rolePermissionEntityList.add(sysRolePermission);
            }
        }//end for


        //删除关系
        sysRolePermissionDao.deleteByRoleId(roleEntity.getRoleId());
        if (!CollectionUtils.isEmpty(rolePermissionEntityList)) {
            // 批量插入

            sysRolePermissionDao.insertBatchSomeColumn(rolePermissionEntityList);
        }
    }


    @Override
    public List<Long> queryRoleIdList(Long createUserId) {
        return baseMapper.queryRoleIdList(createUserId);
    }

    @Override
    public PageNoUtils queryPage(int pageNo, int pageSize, String name, String status, String sortField, String sortOrder) {
        PageNoUtils<SysRoleEntity> pageNoUtils = new PageNoUtils<>();
        Page<SysRoleEntity> page = pageNoUtils.initPageForMP(pageNo, pageSize, sortOrder, sortField);
        QueryWrapper<SysRoleEntity> wrapper = pageNoUtils.initQueryWrapperForNameAndStatus("role_name", name, "status", status);
        baseMapper.selectPage(page, wrapper);
        // 查出page之后，这里要进行处理

        List<SysRoleEntity> entityList = page.getRecords();
        if (CollectionUtils.isEmpty(entityList)) {
            //空的就直接返回了，也不耽误
            return pageNoUtils;
        }

        PageNoUtils pageDTO = new PageNoUtils<>(page.getRecords(), page.getTotal(), page.getSize(), page.getCurrent());
        // 查询角色对应的权限信息
        List<Long> roleIdList = page.getRecords().stream().map(SysRoleEntity::getRoleId).collect(Collectors.toList());

        //这里有roleId的已选择的权限
        // 这里其实有两个东西，一个是roleId,一个是permissionId
        List<SysPermissionEntity> rolePermissionsByRoleIds = sysRolePermissionDao.findRolePermissionsByRoleIds(roleIdList);
        Map<String, String> rolePermissionMap = rolePermissionsByRoleIds.stream().collect(Collectors.toMap(item -> item.getRoleId() + "_" + item.getPermissionId(), SysPermissionEntity::getActions, (oldValue, newValue) -> newValue));

        /**
         * 权限中的全部权限
         * 这里没有适配roleId
         * 因为这里是每一个role都要用到的
         */
        List<SysPermissionEntity> allPermissionList = sysPermissionDao.selectList(null);


        //转成了actionSet
        List<SysPermissionDTO> permissionDTOList = sysPermissionService.setPermissionAllAction(allPermissionList, false);


        // 遍历查询出来的rolePage
        List<RolePermissionDTO> dtoList = page.getRecords().stream().map(record -> {
            RolePermissionDTO dto = new RolePermissionDTO(record);
            String permissionArrayString = JSONArray.toJSONString(permissionDTOList);
            List<SysPermissionDTO> list = JSONArray.parseArray(permissionArrayString, SysPermissionDTO.class);
            for (SysPermissionDTO permission : list) {
                String mapKey = record.getRoleId() + "_" + permission.getPermissionId();
                permission.setActions(rolePermissionMap.getOrDefault(mapKey, ""));
            }
            dto.setPermissions(list);
            return dto;
        }).collect(Collectors.toList());

        pageDTO.setData(dtoList);
        return pageDTO;
    }

    /**
     * 删除角色
     *
     * @param roleId
     */
    @Override
    public void deleteRole(IDDTO roleId) {
        Long longId = IDDTOtoLong(roleId);
        //先查，逻辑和权限的差不多
        SysRoleEntity sysRoleEntity = sysRoleDao.selectById(longId);
        if (ObjectUtils.isEmpty(sysRoleEntity)) {
            throw new GlobalException(Constant.FIND_EMPTY);
        }
        //查角色和用户的关系

        if (sysUserRoleService.selectReleationByRoleId(longId) > 0) {
            throw new GlobalException(Constant.USER_ROLE_FIND);
        }

        if (sysRolePermissionDao.findByRoleId(longId) > 0) {
            throw new GlobalException(Constant.ROLE_PERMISSION_FIND);
        }
        // 如果都没有，就可以进行删除操作了---这里是真删除
        //删除角色
        this.removeById(longId);
    }


    public Long IDDTOtoLong(IDDTO dto) {
        if (ObjectUtils.isEmpty(dto) || StringUtils.isNullOrEmpty(dto.getId())) {
            throw new GlobalException(Constant.PARAM_ERROR);
        }
        String id = dto.getId();
        Long longId = Long.valueOf(id);
        return longId;
    }

    /**
     * 冻结
     *
     * @param roleId
     */
    @Override
    public void freezeRole(IDDTO roleId) {
        Long longId = IDDTOtoLong(roleId);
        sysRoleDao.freezeRole(longId);
    }

    /**
     * 解冻
     *
     * @param roleId
     */
    @Override
    public void unFreezeRole(IDDTO roleId) {
        Long longId = IDDTOtoLong(roleId);
        sysRoleDao.unFreezeRole(longId);
    }

    @Override
    public List<SysUserRoleResponseDTO> roleList() {
        List<SysRoleEntity> list = this.list();
        return list.stream().map(SysUserRoleResponseDTO::new).collect(Collectors.toList());

    }

    /**
     * 检查权限是否越权
     */
//    private void checkPrems(SysRoleEntity role) {
//        //如果不是超级管理员，则需要判断角色的权限是否超过自己的权限
////        if (role.getCreateUserId() == Constant.SUPER_ADMIN) {
////            return;
////        }
//
//        //查询用户所拥有的菜单列表
//        List<Long> menuIdList = sysUserService.queryAllMenuId(role.getCreateUserId());
//
//        //判断是否越权
//        if (!menuIdList.containsAll(role.getMenuIdList())) {
//            throw new GlobalException("新增角色的权限，已超出你的权限范围");
//        }
//    }
}
