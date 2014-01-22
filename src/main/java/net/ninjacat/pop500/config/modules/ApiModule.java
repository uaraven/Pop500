package net.ninjacat.pop500.config.modules;

import net.ninjacat.pop500.api.Api500Px;
import net.ninjacat.pop500.api.PhotoCache;
import net.ninjacat.pop500.api.StreamController;
import org.microba.core.binding.Binder;

import javax.inject.Singleton;

public class ApiModule implements InjectionModule {

    @Override
    public void configure(Binder binder) {

        binder.bind(Api500Px.class).in(Singleton.class);

        binder.bind(PhotoCache.class).in(Singleton.class);

        binder.bind(StreamController.class).in(Singleton.class);
    }
}
