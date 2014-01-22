package net.ninjacat.pop500.api.images;

import net.ninjacat.pop500.api.callbacks.OnBitmapListener;

public interface ImageRetriever {
    void retrieveBitmap(String key, OnBitmapListener listener);
}
