package com.lx.framework.tool.orm.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * @description 多数据源配置类
 * @param null
 * @return: null
 * @author xin.liu
 * @date  10:24
 */
@Configuration
@ConditionalOnProperty(name = { "dynamic.datasource.enabled" }, havingValue = "true")
@MapperScan(basePackages = {"com.lx.framework.**.mapper"},annotationClass = Mapper.class,sqlSessionFactoryRef = "demoDataSourceSqlSessionFactory",sqlSessionTemplateRef = "demoDataSourceSessionTemplate")
public class TestDynamicAutoConfig {

	/**
     * 数据库连接
     * @return
     */
    @Bean(name = "demoDataSource")
    @ConfigurationProperties(prefix = "demo.datasource")
    public DataSource demoDataSource() {
        return DataSourceBuilder.create().build();

    }
    /***
     * mybatis 加载
     * @return
     */
    @Bean(name = "demoDataSourceSqlSessionFactory")
    public MybatisSqlSessionFactoryBean userDataSourceSqlSessionFactory(ApplicationContext applicationContext, @Qualifier("demoDataSource") DataSource dataSource){

    	MybatisSqlSessionFactoryBean sessionFactory = new MybatisSqlSessionFactoryBean();

    	sessionFactory.setDataSource(dataSource);

        List<String> mapperLocations = new ArrayList<>();
		mapperLocations.add("classpath*:mapper/*.xml");
		List<Resource> resources = new ArrayList<>();
		if (mapperLocations != null) {
			for (String mapperLocation : mapperLocations) {
				try {
					Resource[] mappers = applicationContext.getResources(mapperLocation);
					resources.addAll(Arrays.asList(mappers));
				} catch (IOException e) {
					// ignore
				}
			}
		}
		Resource[] res = resources.toArray(new Resource[resources.size()]);
		sessionFactory.setMapperLocations(res);

		MybatisConfiguration configuration = new MybatisConfiguration();
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
	    interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
		configuration.addInterceptor(interceptor);
		sessionFactory.setConfiguration(configuration);

        return sessionFactory;
    }

    @Bean(name = "demoDataSourceSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("demoDataSourceSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name = "demoDataSourceTransactionManager")
	public DataSourceTransactionManager transactionManager(@Qualifier("demoDataSource") DataSource dataSource) {
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
		transactionManager.setDataSource(dataSource);
		return transactionManager;
	}

}
