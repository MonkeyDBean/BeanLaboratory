package com.monkeybean.dynamicds.component.db;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * Created by MonkeyBean on 2019/7/21.
 */
@Configuration
@Order(2)
@MapperScan(basePackages = "com.monkeybean.dynamicds.dao")
public class SqlSessionFactoryConfig {

    private final DynamicDataSource dynamicDataSource;

    @Autowired
    public SqlSessionFactoryConfig(DynamicDataSource dynamicDataSource) {
        this.dynamicDataSource = dynamicDataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactoryConstruct() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dynamicDataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
        bean.setTypeAliasesPackage("com.monkeybean.dynamicds.model");
        return bean.getObject();
    }

}
