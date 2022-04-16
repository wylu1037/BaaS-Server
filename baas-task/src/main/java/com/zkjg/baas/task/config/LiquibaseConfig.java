package com.zkjg.baas.task.config;

import javax.sql.DataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * @author wylu
 * @date 2022/4/15 下午 12:58
 */
@Configuration
public class LiquibaseConfig {
    @Value("${spring.liquibase.changelog-location}")
    private String changeLogLocation;

    /**
     * 是否执行changelog：true-执行、false-不执行，默认值true；
     */
    @Value("${spring.liquibase.enabled:true}")
    private boolean enabled;

    /**
     * 运行环境列表："development,test,production"；
     */
    @Value("${spring.liquibase.contexts:}")
    private String contexts;

    @Bean(name = "liquibase")
    public SpringLiquibase liquibase(@Qualifier("masterDataSource") DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        // 使用的一个master文件引用其他文件的方式，指定changelog的位置
        liquibase.setChangeLog(changeLogLocation);
        liquibase.setShouldRun(enabled);
        if (!StringUtils.isEmpty(contexts)) {
            liquibase.setContexts(contexts);
        }
        return liquibase;
    }
}
