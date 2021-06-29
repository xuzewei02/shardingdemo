package com.xzw.shardingdemo.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.shardingsphere.api.config.sharding.KeyGeneratorConfiguration;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.InlineShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 分库配置
 * 坑1：引入的sharding jar 版本不对有可能导致部分CLASS not found 例：DatabaseType
 */
@Configuration
@MapperScan(basePackages = ShardingDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "shardingSqlSessionFactory")
public class ShardingDataSourceConfig {

    //com.example.shardingsphere.dao.sharding
    static final String PACKAGE = "com.xzw.shardingdemo.goods.dao";  //扫描需要切换分库分表dao层的package
    //classpath:mapper/sharding/*.xml
    static final String MAPPER_LOCATION = "classpath:mapper/*.xml"; //mybatis需要切换数据源的 package

    @Value("${spring.shardingsphere.datasource.g1.url}")
    private String url1;

    @Value("${spring.shardingsphere.datasource.g1.username}")
    private String username1;

    @Value("${spring.shardingsphere.datasource.g1.password}")
    private String password1;

    @Value("${spring.shardingsphere.datasource.g1.driver-class-name}")
    private String driverClass1;

    @Value("${spring.shardingsphere.datasource.g2.url}")
    private String url2;

    @Value("${spring.shardingsphere.datasource.g2.username}")
    private String username2;

    @Value("${spring.shardingsphere.datasource.g2.password}")
    private String password2;

    @Value("${spring.shardingsphere.datasource.g2.driver-class-name}")
    private String driverClass2;

    @Bean(name = "shardingDataSource")
    public DataSource shardingDataSource() throws SQLException {
        Map<String, DataSource> dataSourceMap = new HashMap<>();
        // 配置第一个数据源
        DruidDataSource dataSource1 = new DruidDataSource();
        dataSource1.setDriverClassName(driverClass1);
        dataSource1.setUrl(url1);
        dataSource1.setUsername(username1);
        dataSource1.setPassword(password1);
        dataSourceMap.put("g1", dataSource1);

        // 配置第二个数据源
        DruidDataSource dataSource2 = new DruidDataSource();
        dataSource2.setDriverClassName(driverClass2);
        dataSource2.setUrl(url2);
        dataSource2.setUsername(username2);
        dataSource2.setPassword(password2);
        dataSourceMap.put("g2", dataSource2);

        /**
         * 一.针对业务逻辑表my_sharding_x进行配置
         */
        //水平拆分库 d$ 为application配置
        TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration("my_sharding","g$->{1..2}.my_sharding_$->{1..2}");
        // 配置分库 + 分表策略 + 分布式主键
        //通过user_id分库
        orderTableRuleConfig.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("user_id", "g$->{user_id % 2 + 1}"));
        //通过order_id分表
        orderTableRuleConfig.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("order_id", "my_sharding_$->{order_id % 2 + 1}"));
        orderTableRuleConfig.setKeyGeneratorConfig(new KeyGeneratorConfiguration("SNOWFLAKE", "order_id"));


        /**
         * 二.针对逻辑表broad_table进行配置，该表为广播表（公共表），实际的数据库表名和逻辑表名一致即可
         */

        TableRuleConfiguration broadTableRuleConfig = new TableRuleConfiguration("broad_table");
        broadTableRuleConfig.setKeyGeneratorConfig(new KeyGeneratorConfiguration("SNOWFLAKE", "id"));


        /**
         * 一.针对业务逻辑表my_user_x进行配置
         */
        //水平拆分库 d$ 为application配置
        TableRuleConfiguration userTableRuleConfig = new TableRuleConfiguration("my_user");
        // 配置分库 + 分表策略 + 分布式主键
        //通过user_id分库
        userTableRuleConfig.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("user_id", "g$->{user_id % 2 + 1}"));
        userTableRuleConfig.setKeyGeneratorConfig(new KeyGeneratorConfiguration("SNOWFLAKE", "user_id"));



        /**
         * 三.默认的分库分表规则，如果逻辑表没有指定自己的规则，那么就会被按照以下规则分配
         */

        /**
         * 将分库分表规则配置加入到数据源
         */
        // 添加以上规则，如果有多个逻辑表，可以配置多个加入到shardingRuleConfig
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        // 加入my_sharding
        shardingRuleConfig.getTableRuleConfigs().add(orderTableRuleConfig);
        // 加入广播表
        shardingRuleConfig.getTableRuleConfigs().add(broadTableRuleConfig);
        // 加入my_user
        shardingRuleConfig.getTableRuleConfigs().add(userTableRuleConfig);
        // 加入默认规则
        //shardingRuleConfig.getTableRuleConfigs().add(defaultTableRuleConfig());
        // 获取数据源对象
        DataSource dataSource = ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, getProperties());
        return dataSource;
    }


    /**
     * 系统参数配置
     *
     * @return
     */
    private Properties getProperties() {
        Properties shardingProperties = new Properties();
        shardingProperties.put("sql.show", true);
        return shardingProperties;
    }

    //指定事务对应的数据源管理
    @Bean(name = "shardingTransactionManager")
    public DataSourceTransactionManager shardingTransactionManager(@Qualifier("shardingDataSource") DataSource dataSource) throws SQLException {
        return new DataSourceTransactionManager(dataSource);
    }

    //指定orm 对应的Mybatis xml
    @Bean(name = "shardingSqlSessionFactory")
    public SqlSessionFactory shardingSqlSessionFactory(@Qualifier("shardingDataSource") DataSource dataSource)
            throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(ShardingDataSourceConfig.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }


    //指定mybatis管理的sqlSession
    @Bean(name = "shardingSqlSessionTemplate")
    public SqlSessionTemplate shardingSqlSessionTemplate(@Qualifier("shardingSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
