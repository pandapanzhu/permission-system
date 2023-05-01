package com.shanyu.permission.modules.sys.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shanyu.common.dto.IDDTO;
import com.shanyu.common.exception.GlobalException;
import com.shanyu.common.utils.PageNoUtils;
import com.shanyu.common.utils.StringUtils;
import com.shanyu.permission.modules.sys.config.Constant;
import com.shanyu.permission.modules.sys.dao.SysPermissionDao;
import com.shanyu.permission.modules.sys.dao.SysRolePermissionDao;
import com.shanyu.permission.modules.sys.entity.dto.ActionEntrySet;
import com.shanyu.permission.modules.sys.entity.dto.SysPermissionDTO;
import com.shanyu.permission.modules.sys.entity.dto.SysPermissionRequestDTO;
import com.shanyu.permission.modules.sys.entity.po.SysPermissionEntity;
import com.shanyu.permission.modules.sys.service.SysPermissionService;
import com.shanyu.permission.modules.sys.service.SysUserRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author pan
 */
@Service
public class SysPermissionServiceImpl implements SysPermissionService {

    @Resource
    private SysUserRoleService sysUserRoleService;

    @Resource
    private SysRolePermissionDao sysRolePermissionDao;

    @Resource
    private SysPermissionDao sysPermissionDao;


    /**
     * 获得权限相关信息
     *
     * @param userId
     * @return
     */
    @Override
    public List<SysPermissionDTO> getUserPermissionById(Long userId) {
        //通过id拿到用户的角色关联信息
        List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
        // 根据roleId拿到对应的权限，因为权限是分配在角色上面的
        if (!CollectionUtils.isEmpty(roleIdList)) {
            return getPermissionByRoleIdList(roleIdList);
        }
        return null;
    }

    @Override
    public List<SysPermissionDTO> getPermissionByRoleIdList(List<Long> roleIdList) {
        // 这里是这个人的所有权限
        List<SysPermissionEntity> rolePermissionsByRoleIds = sysRolePermissionDao.findRolePermissionsByRoleIds(roleIdList);

        List<SysPermissionDTO> permissionDTOList = this.setPermissionAllAction(rolePermissionsByRoleIds, false);
        // 进行去重
        Map<String, Set<ActionEntrySet>> map = new HashMap<>(16);
        for (SysPermissionDTO dto : permissionDTOList) {
            Set<ActionEntrySet> actionEntrySets = new HashSet<>(dto.getActionEntitySet());
            if (map.containsKey(dto.getPermissionId())) {
                //如果有这个key了，那就要进行比对action去重
                Set<ActionEntrySet> old = map.get(dto.getPermissionId());
                for (ActionEntrySet action : old) {
                    actionEntrySets.add(action);
                }
            }
            map.put(dto.getPermissionId(), actionEntrySets);
        }// end for
        //那这里的Map就是已经去重过的actions了
        // 现在整理权限，这里同理，已经存在的这里就不要了
        Set<String> permissionIdSet = new HashSet<>();
        List<SysPermissionDTO> dtoList = new ArrayList<>();
        for (SysPermissionDTO dto : permissionDTOList) {
            String permissionId = dto.getPermissionId();
            if (permissionIdSet.contains(permissionId)) {
                // 已经存在了就跳过
            } else {
                // 不存在就set进去
                permissionIdSet.add(permissionId);
                Set<ActionEntrySet> actionSets = map.get(permissionId);
                if (actionSets.size() > 0) {
                    dto.setActionEntitySet(new ArrayList<>(actionSets));
                    dto.setActions(JSONObject.toJSONString(actionSets));
                }
                dtoList.add(dto);
            }
        }
        //actions去重完毕
        return dtoList;
    }

    @Override
    public PageNoUtils getPermissionPage(int pageNo, int pageSize, String permissionName, String status,String sortField, String sortOrder) {
        PageNoUtils<SysPermissionEntity> pageNoUtils = new PageNoUtils<>();
        Page<SysPermissionEntity> page = pageNoUtils.initPageForMP(pageNo, pageSize, sortOrder, sortField);
        QueryWrapper<SysPermissionEntity> wrapper = pageNoUtils.initQueryWrapperForNameAndStatus("permission_name", permissionName, "status", status);

        IPage<SysPermissionEntity> pages = sysPermissionDao.selectPage(page, wrapper);
        //封装分页数据
        // 因为这里查出来的数据就直接打在actions上了，permissionAction是空的
        List<SysPermissionDTO> permissionDTO = this.setPermissionAllAction(pages.getRecords(), false);

        PageNoUtils pageDTO = new PageNoUtils<>(page.getRecords(), page.getTotal(), page.getSize(), page.getCurrent());
        pageDTO.setData(permissionDTO);
        return pageDTO;
    }

    @Override
    public List<SysPermissionDTO> setPermissionAllAction(List<SysPermissionEntity> list, boolean isAll) {
        List<ActionEntrySet> actionEntrySets = loadAllPermissionAction();
        Map<String, ActionEntrySet> actionMap = actionEntrySets.stream().collect(Collectors.toMap(ActionEntrySet::getAction, Function.identity(), (oldValue, newValue) -> newValue));
        List<SysPermissionDTO> permissionDTO = list.stream().map(record -> {
            SysPermissionDTO dto = new SysPermissionDTO(record);
            String actions = isAll ? record.getActionsPermission() : record.getActions();
            if (StringUtils.isNotEmpty(actions)) {
                String[] actionArray = actions.split(",");
                List<ActionEntrySet> actionList = new ArrayList<>();
                for (String action : actionArray) {
                    if (actionMap.containsKey(action)) {
                        actionList.add(actionMap.get(action));
                    }
                }
                dto.setActionEntitySet(actionList);
            }
            return dto;
        }).collect(Collectors.toList());
        return permissionDTO;
    }

    @Override
    public List<ActionEntrySet> loadAllPermissionAction() {
        return sysPermissionDao.loadAllPermissionAction();
    }

    @Override
    public void save(SysPermissionRequestDTO dto) {
        SysPermissionEntity entity = new SysPermissionEntity(dto);
        if (StringUtils.isNullOrEmpty(entity.getActions())) {
            entity.setActions("");
        }
        SysPermissionEntity oldEntity = sysPermissionDao.selectByPermissionId(entity.getPermissionId());
        if (ObjectUtils.isEmpty(oldEntity)) {
            //没查到，那就新增
            if (sysPermissionDao.insert(entity) <= 0) {
                // 新增失败，抛异常
                throw new GlobalException(Constant.INSERT_FAILED);
            }
        } else {
            // 查到了，那就修改
            entity.setId(oldEntity.getId());
            if (sysPermissionDao.updateById(entity) <= 0) {
                // 保存失败，抛异常
                throw new GlobalException(Constant.SAVE_FAILED);
            }
        }
    }


    public void updatePermissionStatus(String permissionId, Integer status) {
        SysPermissionEntity entity = sysPermissionDao.selectByPermissionId(permissionId);
        if (ObjectUtils.isEmpty(entity)) {
            throw new GlobalException(Constant.FIND_EMPTY);
        }
        entity.setStatus(status);
        sysPermissionDao.updateById(entity);
    }

    /**
     * 禁用权限
     *
     * @param id
     */
    @Override
    public void freezePermission(IDDTO id) {
        updatePermissionStatus(id.getId(), Constant.FREEZE_STATUS);
    }


    /**
     * 启用权限
     *
     * @param id
     */
    @Override
    public void unFreezePermission(IDDTO id) {
        updatePermissionStatus(id.getId(), Constant.NORMAL_STATUS);
    }

    /**
     * 删除权限
     * 这里都是真删除
     * 相关信息在操作日志里面看
     *
     * @param id
     */
    @Override
    public void deletePermission(IDDTO id) {
        String permissionId = id.getId();
        SysPermissionEntity entity = sysPermissionDao.selectByPermissionId(permissionId);
        if (ObjectUtils.isEmpty(entity)) {
            throw new GlobalException(Constant.FIND_EMPTY);
        }
        if (sysRolePermissionDao.findByPermissionId(permissionId) > 0) {
            throw new GlobalException(Constant.ROLE_PERMISSION_FIND);
        }
        sysPermissionDao.deleteByPermissionId(permissionId);
    }

    /**
     * 拿到配置好的permission
     *
     * @return
     */
    @Override
    public List<SysPermissionDTO> getPermissionList() {
        List<SysPermissionEntity> sysPermissionEntities = sysPermissionDao.selectList(null);
        List<SysPermissionDTO> permissionDTOList = this.setPermissionAllAction(sysPermissionEntities, false);
        return permissionDTOList;
    }
}
