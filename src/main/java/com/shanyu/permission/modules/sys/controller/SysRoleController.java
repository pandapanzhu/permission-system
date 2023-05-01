

package com.shanyu.permission.modules.sys.controller;

import com.shanyu.common.dto.IDDTO;
import com.shanyu.common.utils.PageNoUtils;
import com.shanyu.permission.common.annotation.SysLog;
import com.shanyu.common.controller.AbstractController;
import com.shanyu.common.utils.Constant;
import com.shanyu.common.utils.PageUtils;
import com.shanyu.common.utils.R;
import com.shanyu.common.validator.ValidatorUtils;
import com.shanyu.permission.modules.sys.entity.SysRoleEntity;
import com.shanyu.permission.modules.sys.entity.dto.RolePermissionDTO;
import com.shanyu.permission.modules.sys.entity.dto.SysRolePermissionRequestDTO;
import com.shanyu.permission.modules.sys.entity.dto.SysUserRoleResponseDTO;
import com.shanyu.permission.modules.sys.service.SysRoleMenuService;
import com.shanyu.permission.modules.sys.service.SysRoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 角色管理
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends AbstractController {
    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 角色列表
     * sortOrder : ascend ,descend
     */
    @GetMapping("/page")
    @RequiresPermissions("sys:role:list")
    public R list(@RequestParam int pageNo,
                  @RequestParam int pageSize,
                  @RequestParam(required = false) String sortField,
                  @RequestParam(required = false) String sortOrder,
                  @RequestParam(required = false) String name,
                  @RequestParam(required = false) String status) {

        PageNoUtils page = sysRoleService.queryPage(pageNo, pageSize, name, status, sortField, sortOrder);

        return R.ok().put("data", page);
    }

    /**
     * 获得角色列表
     *
     * @return
     */
    @GetMapping("/list")
    public R roleList() {
        List<SysUserRoleResponseDTO> list = sysRoleService.roleList();
        return R.ok().put("data", list);
    }

    /**
     * 保存角色
     * 新增和修改角色都在这
     */
    @SysLog("保存角色")
    @PostMapping("/save")
    @RequiresPermissions("sys:role:save")
    public R save(@RequestBody SysRolePermissionRequestDTO dto) {
        ValidatorUtils.validateEntity(dto);
        sysRoleService.saveRole(dto);
        return R.ok();
    }

    /**
     * 删除角色
     */
    @SysLog("删除角色")
    @PostMapping("/delete")
    @RequiresPermissions("sys:role:delete")
    public R delete(@RequestBody IDDTO roleId) {
        sysRoleService.deleteRole(roleId);
        return R.ok();
    }

    /**
     * 冻结角色
     */
    @PostMapping("freeze")
    public R freezeRole(@RequestBody IDDTO roleId) {
        sysRoleService.freezeRole(roleId);
        return R.ok();
    }

    /**
     * 解冻角色
     */
    @PostMapping("unFreeze")
    public R unFreezeRole(@RequestBody IDDTO roleId) {
        sysRoleService.unFreezeRole(roleId);
        return R.ok();
    }

}
