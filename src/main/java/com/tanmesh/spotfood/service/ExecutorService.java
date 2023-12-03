package com.tanmesh.spotfood.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * https://jenkov.com/tutorials/java-util-concurrent/scheduledexecutorservice.html
 *
 */
public class ExecutorService {
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private IFeedService feedService;

    public ExecutorService(IFeedService feedService) {
        this.feedService = feedService;
    }

    final Runnable beeper = new Runnable() {
        public void run() {
//            feedService.generateFeed();
//            feedService.generateExplore();
        }
    };

    public void executorService() {
        final ScheduledFuture<?> beeperHandle = scheduler.scheduleAtFixedRate(beeper, 2, 60*4, SECONDS);
        scheduler.schedule(
                new Runnable() {
                    public void run() {
                        beeperHandle.cancel(true);
                    }
                },
                60 * 60,
                SECONDS
        );
    }
}
