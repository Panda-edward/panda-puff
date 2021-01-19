package com.ed.panda.hunting.config;

import com.ed.panda.hunting.aop.HunterAop;
import com.ed.panda.hunting.aop.PreyAop;
import com.ed.panda.hunting.context.HuntingApplicationContext;
import com.ed.panda.hunting.invoker.PreyInvoker;
import com.ed.panda.hunting.repository.DefaultLocalMsgLogRepository;
import com.ed.panda.hunting.repository.DefaultRetryStrategy;
import com.ed.panda.hunting.repository.ILocalMsgLogRepository;
import com.ed.panda.hunting.repository.IRetryStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.TransactionManager;

/**
 * @author : Edward
 * @date : 2020/11/3 下午12:28
 */
@Configuration
@ConditionalOnClass(TransactionManager.class)
@Slf4j
public class HuntingAutoConfiguration {

    /**
     * 如果应用未提供ITransLogRepository实现,提供基于JDBC的一个默认实现 只在单数据源时,能正常运行
     */
    @Bean
    @ConditionalOnMissingBean(ILocalMsgLogRepository.class)
    public ILocalMsgLogRepository defaultLocalMsgLogRepository(JdbcTemplate jdbcTemplate) {
        log.debug("【hunting】容器中无ILocalMsgLogRepository实现bean,IOC defaultLocalMsgLogRepository...");
        DefaultLocalMsgLogRepository repository = new DefaultLocalMsgLogRepository();
        repository.setJdbcTemplate(jdbcTemplate);
        return repository;
    }

    @Bean
    public IRetryStrategy defaultRetryStrategy() {
        return new DefaultRetryStrategy();
    }


    @Bean
    public HuntingApplicationContext huntingApplicationContext() {
        return new HuntingApplicationContext();
    }

    @Bean
    public PreyInvoker preyInvoker(HuntingApplicationContext context, ILocalMsgLogRepository repository) {
        PreyInvoker preyInvoker = new PreyInvoker();
        preyInvoker.setContext(context);
        preyInvoker.setRepository(repository);
        log.debug("【hunting】PreyInvoker 完成Spring IOC...");
        return preyInvoker;
    }

    @Bean
    public PreyAop preyAop(PreyInvoker preyInvoker, ILocalMsgLogRepository repository) {
        PreyAop preyAop = new PreyAop();
        preyAop.setPreyInvoker(preyInvoker);
        preyAop.setRepository(repository);
        log.debug("【hunting】PreyAop 完成Spring IOC...");
        return preyAop;
    }

    @Bean
    public HunterAop hunterAop() {
        log.debug("【hunting】HunterAop 完成Spring IOC...");
        return new HunterAop();
    }

}
