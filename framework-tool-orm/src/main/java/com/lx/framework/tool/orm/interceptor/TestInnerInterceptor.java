
package com.lx.framework.tool.orm.interceptor;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;


/**
 * Description: sql拦截器  将默认的createBy字段的id转为用户名
 * Date: 2022/6/18 17:15
 * @author xin.liu
 * @version 1.0.0
 */
@Data
@Accessors(chain = true)
@Slf4j
@NoArgsConstructor
public class TestInnerInterceptor implements InnerInterceptor {


    /**
     * {@link Executor(MappedStatement, Object, RowBounds, ResultHandler, BoundSql)} 操作前置处理
     * <p>
     * 改改sql啥的
     *
     * @param executor      Executor(可能是代理对象)
     * @param ms            MappedStatement
     * @param parameter     parameter
     * @param rowBounds     rowBounds
     * @param resultHandler resultHandler
     * @param boundSql      boundSql 执行的SQL语句
     */
    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds,
                            ResultHandler resultHandler, BoundSql boundSql){
        // 获取执行的SQL语句
        String originalSql = boundSql.getSql();
        // SQL语句的参数
        Object parameterObject = boundSql.getParameterObject();
        // 设置改写后的sql
        PluginUtils.MPBoundSql mpBoundSql = PluginUtils.mpBoundSql(boundSql);
        // 改写后的sql
        String rewriteSql="";
        // 如果含有 CREATED_BY、UPDATED_BY 字段，改写sql，完成用户id到用户名的转换
        originalSql=StrUtil.replace(originalSql,"CREATED_BY","REPLACE(CREATED_BY,CREATED_BY,(SELECT NAME FROM `open_api_user` WHERE UUID =CREATED_BY )) AS CREATED_BY");
        originalSql=StrUtil.replace(originalSql,"UPDATED_BY","REPLACE(UPDATED_BY,UPDATED_BY,(SELECT NAME FROM `open_api_user` WHERE UUID =UPDATED_BY )) AS UPDATED_BY");
        mpBoundSql.sql(originalSql);
    }
}
