//package com.lx.framework.tool.startup.utils;
//
//import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
//import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @ClassName JacksonConfig
// * @Description
// * @Author Jun
// * @Date 2023/07/21
// * @Version
// */
//@Configuration
//public class JacksonConfig {
//
//    /***
//     * @MethodName jackson2ObjectMapperBuilderCustomizer
//     * @Description Jackson全局转化long类型为string, 解决jackson序列化时long类型缺失精度问题
//     * @return: org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
//     * @Author Jun
//     * @Date 2023/7/21
//     */
//    @Bean
//    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
//        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder
//                .serializerByType(Long.class, ToStringSerializer.instance)
//                .serializerByType(Long.TYPE, ToStringSerializer.instance);
//    }
//}
