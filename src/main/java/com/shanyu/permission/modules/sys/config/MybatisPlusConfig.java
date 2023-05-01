//
//
//package com.shanyu.permission.modules.sys.config;
//
//import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
//import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//
///**
// * mybatis-plus配置
// */
//@Configuration
//public class MybatisPlusConfig {
//
//    /**
//     * 分页插件
//     */
//    @Bean
//    public PaginationInterceptor paginationInterceptor() {
//        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
//        // 设置最大单页限制数量，默认 500 条，-1 不受限制
//        // 这里也是一个大坑，需要注意的
//        paginationInterceptor.setLimit(-1);
//        // 开启 count 的 join 优化,只针对部分 left join
//        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
//        return paginationInterceptor;
//    }
//
//    @Bean
//    @Primary
//    public MySqlInjector mySqlInjector() {
//        return new MySqlInjector();
//    }
//
//
//}
