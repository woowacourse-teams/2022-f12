package com.woowacourse.f12.config;

import com.woowacourse.f12.application.batch.BatchService;
import com.woowacourse.f12.application.batch.FollowerCountBatchScheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@Configuration
@Profile({"main", "release", "performance"})
public class SchedulerConfig {

    private final BatchService batchService;

    public SchedulerConfig(final BatchService batchService) {
        this.batchService = batchService;
    }

    @Bean(name = "followerCountBatchScheduler")
    public FollowerCountBatchScheduler followerCountBatchScheduler() {
        return new FollowerCountBatchScheduler(batchService);
    }
}
