package net.ninjacat.pop500.api.messages;

import android.graphics.Bitmap;
import net.ninjacat.pop500.api.callbacks.OnBitmapListener;

public final class BitmapRequest {
    private final String url;
    private final OnBitmapListener client;

    public BitmapRequest(String url, OnBitmapListener client) {
        this.url = url;
        this.client = client;
    }

    public String getUrl() {
        return url;
    }

    public OnBitmapListener getClient() {
        return client;
    }

    public BitmapResponse createResponse(Bitmap content) {
        return BitmapResponse.forSuccess(url, content, client);
    }

    public BitmapResponse createResponse(Throwable ex) {
        return BitmapResponse.forFailure(url, ex, client);
    }

}
