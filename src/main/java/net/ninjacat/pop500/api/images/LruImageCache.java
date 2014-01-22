package net.ninjacat.pop500.api.images;

import android.graphics.Bitmap;
import android.util.LruCache;
import net.ninjacat.pop500.logger.Logger;

public class LruImageCache extends LruCache<String, Bitmap> {
    public LruImageCache(long maxSize) {
        super((int) maxSize);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getByteCount();
    }

    @Override
    protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
        super.entryRemoved(evicted, key, oldValue, newValue);
        if (newValue == null) {
            Logger.debug("[LruImageCache] Entry removed. Hits: %d, Misses: %d", hitCount(), missCount());
        }
    }
}
