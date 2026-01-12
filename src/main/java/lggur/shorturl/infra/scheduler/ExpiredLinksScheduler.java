package lggur.shorturl.infra.scheduler;

import lggur.shorturl.application.usecase.ExpireLinksUseCase;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ExpiredLinksScheduler {

    private final ScheduledExecutorService executor =
            Executors.newSingleThreadScheduledExecutor();

    private final ExpireLinksUseCase useCase;
    private final long initialDelay;
    private final long period;
    private final TimeUnit timeUnit;

    public ExpiredLinksScheduler(
            ExpireLinksUseCase useCase,
            long initialDelay,
            long period,
            TimeUnit timeUnit
    ) {
        this.useCase = useCase;
        this.initialDelay = initialDelay;
        this.period = period;
        this.timeUnit = timeUnit;
    }

    public void start() {
        executor.scheduleAtFixedRate(
                useCase::execute,
                initialDelay,
                period,
                timeUnit
        );
    }

    public void stop() {
        executor.shutdown();
    }
}
