package net.ninjacat.pop500.config.modules;

import android.os.Handler;
import net.ninjacat.pop500.config.providers.ExecutorServiceProvider;
import org.microba.core.binding.Binder;

import java.util.concurrent.ExecutorService;

public class Pop500Module implements InjectionModule {
    @Override
    public void configure(Binder binder) {

        binder.bind(ExecutorService.class).toProvider(ExecutorServiceProvider.class);

        binder.bind(Handler.class).toInstance(new Handler());
    }
}
