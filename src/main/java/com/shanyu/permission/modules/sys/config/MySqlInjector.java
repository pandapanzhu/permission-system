//package com.shanyu.permission.modules.sys.config;
//
//import com.baomidou.mybatisplus.core.injector.AbstractMethod;
//import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
//import com.baomidou.mybatisplus.extension.injector.methods.AlwaysUpdateSomeColumnById;
//import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
//import com.baomidou.mybatisplus.extension.injector.methods.Upsert;
//
//import java.util.List;
//
//public class MySqlInjector extends DefaultSqlInjector {
//
//    @Override
//    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
//        List<AbstractMethod> methodList = super.getMethodList(mapperClass);
//        // 添加批量插入方法
//        methodList.add(new InsertBatchSomeColumn());
//        // 更新或者插入
//        methodList.add(new Upsert());
//        //批量更新
//        methodList.add(new AlwaysUpdateSomeColumnById());
//        return methodList;
//    }
//
//}
