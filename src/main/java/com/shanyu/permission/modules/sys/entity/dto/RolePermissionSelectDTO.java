package com.shanyu.permission.modules.sys.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author pan
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionSelectDTO {
    private String name;

    private List<String> actions;

}
