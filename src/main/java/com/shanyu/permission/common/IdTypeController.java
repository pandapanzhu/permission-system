package com.shanyu.permission.common;

import com.alibaba.fastjson.JSONObject;
import com.shanyu.common.contant.IDType;
import com.shanyu.common.utils.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


/**
 * @author pan
 */
@RestController
@RequestMapping("idType")
public class IdTypeController {

    @GetMapping("list")
    public R getIDTypeList() {
        IDType[] idTypes = IDType.values();
        List list = new ArrayList();
        for (IDType id : idTypes) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("key", id.name());
            jsonObject.put("name", id.getName());
            list.add(jsonObject);
        }
        return R.ok().put("idTypes", list);
    }
}
