package com.xzw.shardingdemo.config;

import io.seata.spring.annotation.GlobalTransactionScanner;
import io.seata.spring.annotation.GlobalTransactionalInterceptor;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * seata 注入配置
 */
@Aspect
@Configuration
public class SeataConfig {
    //定义 seata 分布式事务注解
    private static final String AOP_POINTCUT_EXPRESSION = "@annotation(io.seata.spring.annotation.GlobalTransactional)";

    @Value("${seata.txServiceGroup}")
    private String txServiceGroup;

    //接入seata 必须注册Scanner，它负责初始化Transaction Manager（TM：事务管理器）以及Resource Manager（RM：资源管理器）
    @Bean
    public GlobalTransactionScanner getGlobalTransactionScanner() {
        return new GlobalTransactionScanner("my_sharding_demo", txServiceGroup);
    }

    @Bean
    public GlobalTransactionalInterceptor globalTransactionalInterceptor(){
        GlobalTransactionalInterceptor globalTransactionalInterceptor = new GlobalTransactionalInterceptor(null);
        return globalTransactionalInterceptor;
    }

    @Bean
    public Advisor seataAdviceAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
        return new DefaultPointcutAdvisor(pointcut,globalTransactionalInterceptor());
    }

}
