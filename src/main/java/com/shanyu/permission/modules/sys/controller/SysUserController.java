

package com.shanyu.permission.modules.sys.controller;

import com.shanyu.common.dto.IDDTO;
import com.shanyu.common.exception.GlobalException;
import com.shanyu.common.sys.entity.dto.UserDTO;
import com.shanyu.common.utils.PageNoUtils;
import com.shanyu.permission.common.annotation.SysLog;
import com.shanyu.common.controller.AbstractController;
import com.shanyu.common.utils.PageUtils;
import com.shanyu.common.utils.R;
import com.shanyu.common.validator.Assert;
import com.shanyu.common.validator.ValidatorUtils;
import com.shanyu.common.validator.group.AddGroup;
import com.shanyu.common.validator.group.UpdateGroup;
import com.shanyu.permission.modules.sys.config.Constant;
import com.shanyu.permission.modules.sys.entity.SysMenuEntity;
import com.shanyu.permission.modules.sys.entity.SysUserEntity;
import com.shanyu.permission.modules.sys.entity.dto.SysPermissionDTO;
import com.shanyu.permission.modules.sys.entity.dto.SysRolePermissionRequestDTO;
import com.shanyu.permission.modules.sys.entity.dto.SysUserRoleRequestDTO;
import com.shanyu.permission.modules.sys.entity.dto.UserInfoDTO;
import com.shanyu.permission.modules.sys.form.PasswordForm;
import com.shanyu.permission.modules.sys.service.SysMenuService;
import com.shanyu.permission.modules.sys.service.SysPermissionService;
import com.shanyu.permission.modules.sys.service.SysUserRoleService;
import com.shanyu.permission.modules.sys.service.SysUserService;
import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 系统用户
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Resource
    private SysPermissionService sysPermissionService;


    /**
     * 所有用户列表
     */
    @GetMapping("/page")
    @RequiresPermissions("sys:user:list")
    public R list(@RequestParam int pageNo,
                  @RequestParam int pageSize,
                  @RequestParam(required = false) String sortField,
                  @RequestParam(required = false) String sortOrder,
                  @RequestParam(required = false) String name,
                  @RequestParam(required = false) String status) {
        PageNoUtils page = sysUserService.queryPage(pageNo, pageSize, sortField, sortOrder, name, status);
        return R.ok().put("data", page);
    }

    /**
     * 获取登录的用户信息
     * 这个其实是用户的权限信息，不是用户详情
     * 只要一登录就要请求该接口
     */
    @GetMapping("/info")
    public R info() {
        UserDTO userDTO = getUser();
        // 查询用户可用的菜单列表
        List<SysPermissionDTO> menuList = sysPermissionService.getUserPermissionById(getUserId());
        UserInfoDTO dto = UserInfoDTO.builder()
                .email(userDTO.getEmail())
                .createTime(userDTO.getCreateTime())
                .mobile(userDTO.getMobile())
                .status(userDTO.getStatus())
                .username(userDTO.getUsername())
                .permissions(menuList)
                .build();
        return R.ok().put("data", dto);
    }

    /**
     * 修改登录用户密码
     */
    @SysLog("修改密码")
    @PostMapping("/password")
    public R password(@RequestBody PasswordForm form) {
        Assert.isBlank(form.getNewPassword(), "新密码不为能空");
        //sha256加密
        String password = new Sha256Hash(form.getPassword(), getUser().getSalt()).toHex();
        //sha256加密
        String newPassword = new Sha256Hash(form.getNewPassword(), getUser().getSalt()).toHex();

        //更新密码
        boolean flag = sysUserService.updatePassword(getUserId(), password, newPassword);
        if (!flag) {
            return R.error("原密码不正确");
        }
        return R.ok();
    }

    /**
     * 这里只包含了用户的角色信息
     */
    @PostMapping("/userRole")
    @RequiresPermissions("sys:user:info")
    public R info(@RequestBody IDDTO iddto) {
        //获取用户所属的角色列表
        UserDTO userDTO = sysUserService.queryByUserName(iddto.getId());
        if (null == userDTO) {
            throw new GlobalException(Constant.FIND_EMPTY);
        }
        List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userDTO.getUserId());
        return R.ok().put("data", roleIdList);
    }


    /**
     * 保存角色
     * 新增和修改角色都在这
     */
    @SysLog("保存角色")
    @PostMapping("/save")
    @RequiresPermissions("sys:role:save")
    public R save(@RequestBody SysUserRoleRequestDTO dto) {
        ValidatorUtils.validateEntity(dto);

        sysUserService.saveUser(dto, getUserId());
        return R.ok();
    }

    /**
     * 删除角色
     */
    @SysLog("删除角色")
    @PostMapping("/delete")
    @RequiresPermissions("sys:role:delete")
    public R delete(@RequestBody IDDTO iddto) {
        sysUserService.delete(iddto);
        return R.ok();
    }

    /**
     * 冻结角色
     */
    @PostMapping("freeze")
    public R freezeRole(@RequestBody IDDTO iddto) {
        sysUserService.freeze(iddto);
        return R.ok();
    }

    /**
     * 解冻角色
     */
    @PostMapping("unFreeze")
    public R unFreezeRole(@RequestBody IDDTO iddto) {
        sysUserService.unFreeze(iddto);
        return R.ok();
    }
}
