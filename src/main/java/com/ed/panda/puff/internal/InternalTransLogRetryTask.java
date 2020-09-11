package com.ed.panda.puff.internal;

import com.ed.panda.puff.invoker.PuffInvoker;
import com.ed.panda.puff.repository.ITransLogRepository;
import com.ed.panda.puff.repository.PuffTransLog;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * @author : Edward
 * @date : 2020/9/11 上午11:53
 */
@Setter
@Slf4j
public class InternalTransLogRetryTask {

    private ITransLogRepository transLogRepository;

    private PuffInvoker puffInvoker;

    private int limit;

    /**
     * 重试未成功TransLog
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void retry() {
        List<PuffTransLog> transLogs = transLogRepository.queryRetryTranLogs(limit);
        log.info("内置重试任务执行====>count:{}", transLogs.size());
        transLogs.forEach(x -> puffInvoker.asyncInvoke(x));
    }
}
