

package com.shanyu.permission.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shanyu.common.dto.IDDTO;
import com.shanyu.common.sys.entity.dto.UserDTO;
import com.shanyu.common.utils.PageNoUtils;
import com.shanyu.permission.modules.sys.entity.SysUserEntity;
import com.shanyu.permission.modules.sys.entity.dto.SysUserRoleRequestDTO;


/**
 * 系统用户
 * @author pan
 */
public interface SysUserService extends IService<SysUserEntity> {

    /**
     * 系统用户的分页查询
     * @param pageNo
     * @param pageSize
     * @param sortField
     * @param sortOrder
     * @param name
     * @param status
     * @return
     */
    PageNoUtils queryPage(int pageNo, int pageSize, String sortField, String sortOrder, String name, String status);
    
    

    /**
     * 根据用户名，查询系统用户
     */
    UserDTO queryByUserName(String username);

    /**
     * 保存用户
     */
    void saveUser(SysUserRoleRequestDTO user, Long userId);
    

    /**
     * 修改密码
     *
     * @param userId      用户ID
     * @param password    原密码
     * @param newPassword 新密码
     */
    boolean updatePassword(Long userId, String password, String newPassword);

    int delete(IDDTO roleId);

    int freeze(IDDTO iddto);

    int unFreeze(IDDTO iddto);
}
