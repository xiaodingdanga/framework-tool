//package com.lx.framework.tool.orm.config;
//
//import com.baomidou.mybatisplus.annotation.DbType;
//import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
//import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
///**
// * Description: MyBatis-Plus 配置类
// * Date: 2022/4/20 11:44
// * @author xin.liu
// * @version 1.0.0
// */
//@Configuration
//@EnableTransactionManagement
//@MapperScan("com.lx.framework.**.mapper")
//public class MyBatisPlusConfig {
//
//    /**
//     * Description 新的分页插件,一缓和二缓遵循mybatis的规则
//     *             需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题(该属性会在旧插件移除后一同移除)
//     * @return com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor
//     * @author xin.liu
//     * @date   2022/4/20 11:51
//     */
//    @Bean
//    public MybatisPlusInterceptor mybatisPlusInterceptor() {
//        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
//        //创建乐观锁拦截器 OptimisticLockerInnerInterceptor
////        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
////         添加自定义的拦截器
////        interceptor.addInnerInterceptor(new TestInnerInterceptor());
//        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
//        return interceptor;
//    }
//
//}
