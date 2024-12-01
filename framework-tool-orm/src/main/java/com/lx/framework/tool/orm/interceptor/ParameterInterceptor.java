package com.lx.framework.tool.orm.interceptor;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.MybatisParameterHandler;
import com.lx.framework.tool.orm.annotation.SensitiveData;
import com.lx.framework.tool.orm.annotation.SensitiveField;
import com.lx.framework.tool.orm.utils.EncryptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * @author xin.liu
 * @description sql执行前的前置拦截器:用于加密
 * @date 2024-04-03  15:41
 * @Version 1.0
 */
@Slf4j
@Component
@Intercepts({
    @Signature(type = ParameterHandler.class, method = "setParameters", args = PreparedStatement.class),
})
public class ParameterInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // @Signature 指定了 type= parameterHandler 后，这里的 invocation.getTarget() 便是parameterHandler
        // 若指定ResultSetHandler ，这里则能强转为ResultSetHandler
        MybatisParameterHandler parameterHandler = (MybatisParameterHandler) invocation.getTarget();
        // 获取参数对像，即 mapper 中 paramsType 的实例
        Field parameterField = parameterHandler.getClass().getDeclaredField("parameterObject");
        parameterField.setAccessible(true);
        // 取出参数实例
        Object parameterObject = parameterField.get(parameterHandler);

        if (parameterObject != null) {
            // 1、对类字段进行加密：校验该实例的类是否被@SensitiveData所注解
            processSensitiveDataParam(parameterObject);

            // 2、如果传参为List类型，对list里的对象进行加密
            processListParam(parameterObject);

            // 3、 处理普通参数
            processNormalParam(parameterHandler, parameterObject);
        }
        return invocation.proceed();
    }



    /**
     * @description 处理普通参数进行加密处理   @SensitiveField @Param("mobile") String mobile
     * @param parameterHandler
     * @param parameterObject
     * @return: void
     * @author xin.liu
     * @date 2024/4/6 15:55
     */
    private void processNormalParam(ParameterHandler parameterHandler,Object parameterObject)
            throws NoSuchFieldException, ClassNotFoundException, IllegalAccessException {
        Class<MybatisParameterHandler> handlerClass = MybatisParameterHandler.class;
        Field mappedStatementFiled = handlerClass.getDeclaredField("mappedStatement");
        mappedStatementFiled.setAccessible(true);
        MappedStatement mappedStatement = (MappedStatement) mappedStatementFiled.get(parameterHandler);
        String methodName = mappedStatement.getId();
        Class<?> mapperClass = Class.forName(methodName.substring(0, methodName.lastIndexOf('.')));
        methodName = methodName.substring(methodName.lastIndexOf('.') + 1);
        Method[] methods = mapperClass.getDeclaredMethods();
        Method method = null;
        for (Method m : methods) {
            if (m.getName().equals(methodName)) {
                method = m;
                break;
            }
        }
        if (method != null) {
            // 获取方法参数注解列表，每个方法有多个参数，每个参数有多个注解，所以这里是二维数组
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            // 获取方法的参数列表
            Parameter[] parameters = method.getParameters();
           for(Parameter parameter:parameters){
               // 使用了@SensitiveField注解的普通参数，进行直接加密处理
               SensitiveField sensitiveField=parameter.getAnnotation(SensitiveField.class);
               if(Objects.nonNull(sensitiveField)){
                   if (parameterObject instanceof Map) {
                       @SuppressWarnings("unchecked")
                       Map<String, String> map = ((Map<String, String>) parameterObject);
                       String value=map.get(parameter.getName());
                       if(StrUtil.isNotBlank(value)){
                           map.put(parameter.getName(), EncryptionUtil.encrypt(value));
                       }
                   }
               }
            }
        }
    }



    /**
     * @description 处理被@SensitiveData注解标注的参数
     * @param parameterObject
     * @return: void
     * @author xin.liu
     * @date 2024/4/6 15:22
     */
    private void processSensitiveDataParam(Object parameterObject) throws IllegalAccessException {
        Class<?> parameterObjectClass = parameterObject.getClass();
        SensitiveData sensitiveData = AnnotationUtils.findAnnotation(parameterObjectClass, SensitiveData.class);
        if (Objects.nonNull(sensitiveData)) {
            // 取出当前当前类所有字段，传入加密方法
            Field[] declaredFields = parameterObjectClass.getDeclaredFields();
            EncryptionUtil.encrypt(declaredFields, parameterObject);
        }
    }


    /**
     * @description 如果传参为List类型，对list里的对象判断，是否进行加密
     * @param parameterObject
     * @return: void
     * @author xin.liu
     * @date 2024/4/3 15:43
     */
    private void processListParam(Object parameterObject) throws IllegalAccessException {
        // mybatis会把list封装到一个ParamMap中
        if (parameterObject instanceof Map) {
            // 1、如果不对传参users使用@Param注解，则会在map中放入collection、list、users三个键值对，这三个指向同一个内存地址即内容相同。
            if (((Map) parameterObject).containsKey("list")) {
                Map map = (Map) parameterObject;
                ArrayList list = (ArrayList) map.get("list");
                Object element = list.get(0);
                Class<?> elementClass = element.getClass();
                SensitiveData tempSensitiveData = AnnotationUtils.findAnnotation(elementClass, SensitiveData.class);
                if (Objects.nonNull(tempSensitiveData)) {
                    for (Object elementObject : list) {
                        Field[] declaredFields = elementClass.getDeclaredFields();
                        EncryptionUtil.encrypt(declaredFields, elementObject);
                    }
                }
            }

            // 2、如果使用了@Param注解对参数重命名为users，那么map中只会放入users、param1两个键值对，这2个指向同一个内存地址即内容相同。
            if (((Map) parameterObject).containsKey("param1")) {
                Map map = (Map) parameterObject;
                Object param1 = map.get("param1");
                //如果param1是ArrayList,则转为arrayList。否则不转换
                if (param1 instanceof ArrayList) {
                    ArrayList list = (ArrayList) param1;
                    Object element = list.get(0);
                    Class<?> elementClass = element.getClass();
                    SensitiveData tempSensitiveData = AnnotationUtils.findAnnotation(elementClass, SensitiveData.class);
                    if (Objects.nonNull(tempSensitiveData)) {
                        for (Object elementObject : list) {
                            Field[] declaredFields = elementClass.getDeclaredFields();
                            EncryptionUtil.encrypt(declaredFields, elementObject);
                        }
                    }
                } else if(ObjectUtil.isNotEmpty(param1)){
                    Class<?> elementClass = param1.getClass();
                    SensitiveData tempSensitiveData = AnnotationUtils.findAnnotation(elementClass, SensitiveData.class);
                    if (Objects.nonNull(tempSensitiveData)) {
                        Field[] declaredFields = elementClass.getDeclaredFields();
                        EncryptionUtil.encrypt(declaredFields, param1);
                    }
                }
            }
        }
    }


    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
