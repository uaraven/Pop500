package net.ninjacat.pop500.api.images;

import android.graphics.Bitmap;
import net.ninjacat.pop500.api.callbacks.OnBitmapListener;

public interface ImageCache {
    void storeBitmap(String key, Bitmap image);

    boolean hasBitmap(String key);

    Bitmap retrieveBitmap(String key);
}
