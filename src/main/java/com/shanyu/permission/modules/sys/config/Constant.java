package com.shanyu.permission.modules.sys.config;

/**
 * @author pan
 */
public interface Constant {

    /**
     * 约定的密钥
     */
    String AES_KEY = "E08ADE2612744B17";

    String INSERT_FAILED = "新增失败，请联系管理员";
    String SAVE_FAILED = "保存失败，请联系管理员";
    String FIND_EMPTY = "未找到相关数据，请联系管理员";
    String PARAM_ERROR = "参数错误";
    Integer NORMAL_STATUS = 1;
    Integer FREEZE_STATUS = 0;
    Integer DELETE_STATUS = -1;

    /**
     * 权限相关的提示信息
     */
    String ROLE_PERMISSION_FIND = "存在权限与角色的绑定关系，请先删除该关系";
    String USER_ROLE_FIND = "存在用户与角色的绑定关系，请先删除该关系";

    /**
     * 排序相关，正序和反序
     */
    String ASC_END = "ascend";
    String DESC_END = "descend";


}
