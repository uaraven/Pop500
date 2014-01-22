package net.ninjacat.pop500.api.images;

import android.graphics.Bitmap;
import net.ninjacat.pop500.api.callbacks.OnBitmapListener;

import javax.inject.Inject;

public class InMemoryImageCache implements ImageCache {

    private final LruImageCache cache;

    @Inject
    public InMemoryImageCache(LruImageCache cache) {
        this.cache = cache;
    }

    @Override
    public void storeBitmap(String key, Bitmap image) {
        cache.put(key, image);
    }

    @Override
    public boolean hasBitmap(String key) {
        return cache.get(key) != null;
    }

    @Override
    public Bitmap retrieveBitmap(String key) {
        return cache.get(key);
    }


}
