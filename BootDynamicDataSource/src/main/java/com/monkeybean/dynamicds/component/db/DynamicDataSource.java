package com.monkeybean.dynamicds.component.db;

import com.monkeybean.dynamicds.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 动态数据源, 常用于读写分离, 一般通过aop注解设置具体使用的数据源
 * <p>
 * Created by MonkeyBean on 2019/7/21.
 */
@Component
@Order(1)
@Slf4j
public final class DynamicDataSource extends AbstractRoutingDataSource {

    /**
     * 主数据源标记
     */
    private static final String masterDataSourceKey = "primary";

    /**
     * 存储当前数据源的标记, 如 primary, secondary
     * 线程本地类型, 仅当前线程可以访问, 线程安全, 各线程互不影响
     */
    private static final ThreadLocal<String> dataSourceHolder = new ThreadLocal<>();
    /**
     * 除去主数据源的标记数组
     */
    private final static List<String> slaveDataSourceKeys = new ArrayList<>();
    /**
     * 用于随机从数据源标记
     */
    private final static Random indexRandom = new Random(System.currentTimeMillis());
    /**
     * 数据源标记数组
     */
    private static List<String> dataSourceKeys;

    /**
     * 设置当前数据源为主数据源
     */
    public static void setMasterAsDataSource() {
        setCurrentDataSourceKey(masterDataSourceKey);
    }

    /**
     * 设置当前数据源为任一从数据源
     */
    public static void setSlaveAsDataSource() {
        setCurrentDataSourceKey(slaveDataSourceKeys.get(indexRandom.nextInt(slaveDataSourceKeys.size())));
    }

    /**
     * 获取当前数据源标记
     */
    public static String getCurrentDataSourceKey() {
        return dataSourceHolder.get();
    }

    /**
     * 设置当前需使用的数据源, 如 primary
     */
    private static void setCurrentDataSourceKey(String dataSourceName) {
        if (dataSourceKeys.contains(dataSourceName)) {
            dataSourceHolder.set(dataSourceName);
        }
    }

    /**
     * 获取数据源标记数组
     */
    public static List<String> getDataSourceKeys() {
        return dataSourceKeys;
    }

    /**
     * 被@PostConstruct修饰的方法会在服务器加载Servlet时执行, 并且只会被服务器调用一次, 在构造函数之后执行
     */
    @PostConstruct
    private void initDataSources() {
        final String dbConfigPrefix = "datasource";
        Map<Object, Object> dataSources = new HashMap<>();
        Properties props = PropertiesUtil.getProperties();
        dataSourceKeys = new ArrayList<>();

        //打印配置参数
        for (Map.Entry<Object, Object> entry : props.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            log.info("prop key: [{}], value: [{}]", key, value);
        }

        //遍历筛选数据源相关配置
        Enumeration<?> enu = props.propertyNames();
        while (enu.hasMoreElements()) {
            String key = enu.nextElement().toString();
            if (key.startsWith(dbConfigPrefix)) {

                //截取第二个.之前的子串
                key = key.substring(key.indexOf(".") + 1, key.indexOf(".", key.indexOf(".") + 1));
                dataSourceKeys.add(key);
            }
        }

        //去重
        dataSourceKeys = dataSourceKeys.stream().distinct().collect(Collectors.toList());

        //设置数据源
        for (String key : dataSourceKeys) {
            BasicDataSource dataSource = new BasicDataSource();
            dataSource.setUrl(props.getProperty(dbConfigPrefix + "." + key + ".jdbcUrl"));
            dataSource.setUsername(props.getProperty(dbConfigPrefix + "." + key + ".username"));
            dataSource.setPassword(props.getProperty(dbConfigPrefix + "." + key + ".password"));
            dataSource.setDriverClassName(props.getProperty(dbConfigPrefix + "." + key + ".driver-class-name"));
            dataSources.put(key, dataSource);

            //默认数据源
            if (key.equals(masterDataSourceKey)) {
                setDefaultTargetDataSource(dataSource);
            }
        }
        setTargetDataSources(dataSources);
        afterPropertiesSet();

        //从数据源标记数组设置
        slaveDataSourceKeys.addAll(dataSourceKeys);
        slaveDataSourceKeys.remove(masterDataSourceKey);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return dataSourceHolder.get();
    }

}
