package net.ninjacat.pop500.api.images;

import android.graphics.Bitmap;

public interface ImageCache {
    void storeBitmap(String key, Bitmap image);

    boolean hasBitmap(String key);

    Bitmap retrieveBitmap(String key);
}
