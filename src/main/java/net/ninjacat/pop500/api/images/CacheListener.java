package net.ninjacat.lista.images;

import android.graphics.Bitmap;

/**
 * User: raven
 * Date: 06.10.12
 */
public interface CacheListener {
    void imageAvailable(String key, Bitmap bitmap);
}
