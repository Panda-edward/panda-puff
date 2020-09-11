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

    /**
     * 是否启用内置重试task: 推荐使用统一的调度平台
     */
    private TaskConfig task;

    private ExecutorConfig globalExecutor;

    private Map<Integer, ExecutorConfig> bizExecutor;

    @Data
    public static class ExecutorConfig {

        private int poolSize;
    }

    @Data
    public static class TaskConfig {

        private boolean enableInternal;

        private int limit;
    }

}
