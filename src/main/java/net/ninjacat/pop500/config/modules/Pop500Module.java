package net.ninjacat.pop500.config.modules;

import android.os.Handler;
import net.ninjacat.pop500.api.Queued;
import net.ninjacat.pop500.config.providers.ExecutorServiceProvider;
import org.microba.core.binding.Binder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Pop500Module implements InjectionModule {
    @Override
    public void configure(Binder binder) {

        binder.bind(ExecutorService.class).qualifiedWith(Queued.class).toInstance(Executors.newFixedThreadPool(1));
        binder.bind(ExecutorService.class).toProvider(ExecutorServiceProvider.class);

        binder.bind(Handler.class).toInstance(new Handler());
    }
}
