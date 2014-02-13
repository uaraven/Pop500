package net.ninjacat.pop500.api.callbacks;

import android.graphics.Bitmap;

public interface OnBitmapListener {
    void bitmapAvailable(String key, Bitmap bitmap);

    void bitmapFailed(Throwable fail);
}
