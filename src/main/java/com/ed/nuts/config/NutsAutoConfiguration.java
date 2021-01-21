package com.ed.nuts.config;

import com.ed.nuts.NutsAop;
import com.ed.nuts.NutsContext;
import com.ed.nuts.NutsHandler;
import com.ed.nuts.repository.INutsRepository;
import com.ed.nuts.repository.JdbcTemplateNutsRepository;
import com.ed.nuts.repository.MybatisNutsRepository;
import com.ed.nuts.repository.mapper.NutsMapper;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.TransactionManager;

/**
 * @author : Edward
 * @date : 2021/1/20 上午10:53
 */
@Configuration
@MapperScan(basePackages = "com.ed.nuts.repository.mapper")
@ConditionalOnClass(TransactionManager.class)
@Slf4j
public class NutsAutoConfiguration {

    @Bean
    public NutsContext nutsContext() {
        return new NutsContext();
    }

    /**
     * 如果应用未提供ITransLogRepository实现,提供默认实现
     */
//    @Bean
//    @ConditionalOnMissingBean({INutsRepository.class,NutsMapper.class})
//    public INutsRepository nutsRepository(JdbcTemplate jdbcTemplate) {
//        log.info("【nuts】容器中无INutsRepository实现bean,注入默认实现");
//        JdbcTemplateNutsRepository repository = new JdbcTemplateNutsRepository();
//        repository.setJdbcTemplate(jdbcTemplate);
//        return repository;
//    }

    @Bean
    @ConditionalOnMissingBean(INutsRepository.class)
    public INutsRepository nutsRepository(NutsMapper nutsMapper) {
        log.info("【nuts】容器中无INutsRepository实现bean,注入默认实现");
        MybatisNutsRepository repository = new MybatisNutsRepository();
        repository.setNutsMapper(nutsMapper);
        return repository;
    }

    @Bean
    public NutsHandler nutsHandler(NutsContext context, INutsRepository nutsRepository) {
        NutsHandler handler = new NutsHandler();
        handler.setContext(context);
        handler.setNutsRepository(nutsRepository);
        return handler;
    }

    @Bean
    public NutsAop nutsAop(NutsHandler handler, INutsRepository nutsRepository, NutsContext context) {
        NutsAop nutsAop = new NutsAop();
        nutsAop.setHandler(handler);
        nutsAop.setNutsRepository(nutsRepository);
        nutsAop.setContext(context);
        return nutsAop;
    }
}
