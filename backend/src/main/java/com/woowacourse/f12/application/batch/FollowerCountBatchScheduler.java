package com.woowacourse.f12.application.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
public class FollowerCountBatchScheduler {

    private static final String LOG_FORMAT = "Class : {}, Message : {}";

    private final BatchService batchService;

    public FollowerCountBatchScheduler(final BatchService batchService) {
        this.batchService = batchService;
    }

    @Scheduled(cron = "0 0 0/1 1/1 * ? *")
    public void execute() {
        try {
            batchService.updateFollowerCount();
        } catch (Exception e) {
            log.error(LOG_FORMAT, e.getClass().getSimpleName(), e.getMessage());
        }
    }
}
