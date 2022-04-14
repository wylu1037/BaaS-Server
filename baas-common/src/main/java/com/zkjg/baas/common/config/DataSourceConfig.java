package com.zkjg.baas.common.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author luwenyang
 * @date 2022/4/13 下午 01:56
 */
@Slf4j
@Configuration
public class DataSourceConfig {

    @Value("spring.datasource.druid.username")
    private String userName;
    @Value("spring.datasource.druid.password")
    private String password;

    @Primary
    @Bean(name = "masterDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DataSource masterDataSource() {
        log.info("[DataSourceConfig] masterDataSource() start create master database!");
        return DruidDataSourceBuilder.create().build();
    }

    @Primary
    @Bean("jdbcTemplate")
    public JdbcTemplate jdbcTemplate(@Qualifier("masterDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
