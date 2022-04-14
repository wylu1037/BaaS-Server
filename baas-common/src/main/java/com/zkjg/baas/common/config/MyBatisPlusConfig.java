package com.zkjg.baas.common.config;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * @author luwenyang
 * @date 2022/4/13 下午 02:09
 */
@Configuration
public class MyBatisPlusConfig {

    /**
     * 拦截器
     *
     * @return
     */
    @Bean("mybatisPlusInterceptor")
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 拦截器容器
        List<InnerInterceptor> list = new ArrayList<>();
        // 分页插件拦截器
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        list.add(paginationInnerInterceptor);

        // 防止全表更新与删除拦截器
        BlockAttackInnerInterceptor blockAttackInnerInterceptor = new BlockAttackInnerInterceptor();
        list.add(blockAttackInnerInterceptor);

        interceptor.setInterceptors(list);
        return interceptor;
    }

    @Bean("sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("masterDataSource") DataSource dataSource,
        @Qualifier("mybatisPlusInterceptor") MybatisPlusInterceptor mybatisPlusInterceptor) throws Exception {
        MybatisSqlSessionFactoryBean sessionFactory = new MybatisSqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);

        MybatisConfiguration configuration = new MybatisConfiguration();
        // configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setCacheEnabled(true);
        // 解决属性大写变小写加下划线的问题
        configuration.setMapUnderscoreToCamelCase(false);
        configuration.setCallSettersOnNulls(true);
        // 正式环境需要屏蔽：MybatisPlus执行SQL时，将其打印出来
        // configuration.setLogImpl(org.apache.ibatis.logging.stdout.StdOutImpl.class);
        sessionFactory.setConfiguration(configuration);

        // 更新策略：update语句中避免字段为空不更新
        GlobalConfig globalConfig = GlobalConfigUtils.defaults();
        // IGNORED：忽略、NOT_NULL：默认策略，非null、NOT_EMPTY：非empty
        globalConfig.getDbConfig().setUpdateStrategy(FieldStrategy.IGNORED);
        sessionFactory.setGlobalConfig(globalConfig);

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setMapperLocations(resolver.getResources("classpath*:mappers/*.xml"));

        // 设置别名包扫描
        // sessionFactory.setTypeAliasesPackage(typeAliasesPackage);
        // 设置插件
        sessionFactory.setPlugins(mybatisPlusInterceptor);
        return sessionFactory.getObject();
    }
}
