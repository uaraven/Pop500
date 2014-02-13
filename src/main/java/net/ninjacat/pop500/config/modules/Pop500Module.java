package net.ninjacat.pop500.config.modules;

import android.os.Handler;
import org.microba.core.binding.Binder;

public class Pop500Module implements InjectionModule {
    @Override
    public void configure(Binder binder) {
        binder.bind(Handler.class).toInstance(new Handler());
    }
}
