package net.ninjacat.pop500.api;

import android.support.v4.util.LruCache;

/**
 * Created on 21/01/14.
 */
public class PhotoCache extends LruCache<Integer, Photo> {

    private static final int PHOTO_CACHE_SIZE = 250 * 1024;

    public PhotoCache() {
        super(PHOTO_CACHE_SIZE);
    }

    public boolean hasKey(Integer key) {
        return this.get(key) != null;
    }
}
