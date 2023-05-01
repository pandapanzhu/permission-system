package com.shanyu.permission.modules.sys.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pan
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActionEntrySet {

    /**
     * 具体的动作
     */
    private String action;

    /**
     * 动作描述
     */
    private String describe;

    /**
     * 是否是默认检查
     */
    private boolean defaultCheck;
}
