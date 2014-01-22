package net.ninjacat.pop500.config.modules;

import net.ninjacat.pop500.api.images.*;
import org.microba.core.binding.Binder;

import javax.inject.Singleton;

public class ImagesModule implements InjectionModule {
    private static final int IMAGE_CACHE_MEMORY_FACTOR = 3;
    private final long memoryPerApplication;

    public ImagesModule(long memoryPerApplication) {
        this.memoryPerApplication = memoryPerApplication;
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(LruImageCache.class).toInstance(new LruImageCache(memoryPerApplication / IMAGE_CACHE_MEMORY_FACTOR));

        binder.bind(ImageRetriever.class).to(ApiImageRetriever.class);
        binder.bind(ImageCache.class).to(InMemoryImageCache.class).in(Singleton.class);
    }
}
