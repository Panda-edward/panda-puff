package com.ed.panda.puff.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author : Edward
 * @date : 2020/9/10 上午11:44
 */
@ConfigurationProperties(prefix = PuffProperties.PUFF_PREFIX)
@Data
public class PuffProperties {

    public static final String PUFF_PREFIX = "puff";

    private ExecutorConfig globalExecutor;

    private Map<Integer,ExecutorConfig> bizExecutor;

    @Data
    public static class ExecutorConfig {
        private int poolSize;
    }
}
