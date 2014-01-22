package net.ninjacat.pop500.config.providers;


import javax.inject.Provider;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceProvider implements Provider<ExecutorService> {

    private static final int MAX_WORKER_THREADS = 4;

    @Override
    public ExecutorService get() {
        return Executors.newFixedThreadPool(MAX_WORKER_THREADS);
    }
}
