package net.ninjacat.pop500.config;

import android.app.ActivityManager;
import android.content.Context;
import net.ninjacat.pop500.config.modules.ApiModule;
import net.ninjacat.pop500.config.modules.ImagesModule;
import net.ninjacat.pop500.config.modules.Pop500Module;
import net.ninjacat.pop500.logger.Logger;
import org.microba.core.Microba;
import org.microba.core.MicrobaContext;
import org.microba.core.binding.Binder;
import org.microba.core.injection.InjectionContext;

/**
 * User: raven
 * Date: 05.10.12
 */
public class Injector {
    private final MicrobaContext injectionContext;

    public Injector(Context context, long availableMemory) {
        Binder binder = Microba.createBinder();

        binder.bind(Context.class).toInstance(context);

        new Pop500Module().configure(binder);
        new ApiModule().configure(binder);
        new ImagesModule(availableMemory).configure(binder);

        injectionContext = Microba.createContext(binder);
    }

    public void injectMembers(Object target) {
        injectionContext.getInjectionContext().injectMembers(target);
    }

    public <T> T get(Class<? extends T> clazz) {
        return injectionContext.getInjectionContext().getProvider(clazz).get();
    }

    public InjectionContext getContext() {
        return injectionContext.getInjectionContext();
    }
}

