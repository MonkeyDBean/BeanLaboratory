package com.monkeybean.labo.component.config.database;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * 用于测试预先配置的多数据源
 * <p>
 * Created by MonkeyBean on 2018/05/26.
 */
@Configuration
@MapperScan(value = "com.monkeybean.labo.dao.third", sqlSessionFactoryRef = "thirdSqlSessionFactory")
public class ThirdDataSourceConfig {

    @Bean(name = "thirdDatasource")
    @ConfigurationProperties(prefix = "datasource.third")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "thirdSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryPrimary(@Qualifier("thirdDatasource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/ThirdDataDaoMapper.xml"));
        return bean.getObject();
    }
}
