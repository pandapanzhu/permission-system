package com.shanyu.permission.modules.sys.controller;

import com.shanyu.common.dto.IDDTO;
import com.shanyu.common.utils.PageNoUtils;
import com.shanyu.common.utils.R;
import com.shanyu.permission.common.annotation.SysLog;
import com.shanyu.permission.modules.sys.entity.dto.ActionEntrySet;
import com.shanyu.permission.modules.sys.entity.dto.SysPermissionDTO;
import com.shanyu.permission.modules.sys.entity.dto.SysPermissionRequestDTO;
import com.shanyu.permission.modules.sys.service.SysPermissionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 权限管理相关接口在这里
 *
 * @author pan
 */
@RestController
@RequestMapping("/sys/permission")
public class SysPermissionController {

    @Resource
    private SysPermissionService sysPermissionService;

    @GetMapping("page")
    public R getPermissionPage(@RequestParam int pageNo,
                               @RequestParam int pageSize,
                               @RequestParam(required = false) String permissionName,
                               @RequestParam(required = false) String status,
                               @RequestParam(required = false) String sortField,
                               @RequestParam(required = false) String sortOrder) {

        PageNoUtils page = sysPermissionService.getPermissionPage(pageNo, pageSize, permissionName, status, sortField, sortOrder);
        return R.ok().put("data", page);
    }

    /**
     * 加载全部的actions
     *
     * @return
     */
    @GetMapping("loadAllPermissionAction")
    public R loadAllPermissionAction() {
        List<ActionEntrySet> actions = sysPermissionService.loadAllPermissionAction();
        return R.ok().put("data", actions);
    }

    /**
     * 这里是拿所有的permission
     *
     * @return
     */
    @GetMapping("list")
    public R getPermissionList() {
        List<SysPermissionDTO> permissions = sysPermissionService.getPermissionList();
        return R.ok().put("data", permissions);
    }

    /**
     * 只需要传id,name,descrebe和actions
     *
     * @param dto
     * @return
     */
    @PostMapping("savePermission")
    public R savePermission(@RequestBody SysPermissionRequestDTO dto) {
        sysPermissionService.save(dto);
        return R.ok();
    }

    /**
     * 只需要传id
     *
     * @return
     */
    @PostMapping("freeze")
    public R freezePermission(@RequestBody IDDTO id) {
        sysPermissionService.freezePermission(id);
        return R.ok();
    }

    /**
     * 只需要传id
     *
     * @return
     */
    @PostMapping("unFreeze")
    public R unFreezePermission(@RequestBody IDDTO id) {
        sysPermissionService.unFreezePermission(id);
        return R.ok();
    }

    /**
     * 只需要传id
     * 这里要检查是否有依赖才能删除
     *
     * @return
     */
    @SysLog("删除权限")
    @PostMapping("delete")
    public R deletePermission(@RequestBody IDDTO id) {
        sysPermissionService.deletePermission(id);
        return R.ok();
    }
}
