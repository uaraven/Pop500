package net.ninjacat.pop500.api.messages;

import android.graphics.Bitmap;
import net.ninjacat.drama.Option;
import net.ninjacat.pop500.api.callbacks.OnBitmapListener;

public final class BitmapResponse {
    private final String url;
    private final Option<Bitmap> bitmap;
    private final Throwable fail;
    private final OnBitmapListener client;

    public static BitmapResponse forSuccess(String url, Bitmap bitmap, OnBitmapListener client) {
        return new BitmapResponse(url, bitmap, null, client);
    }

    public static BitmapResponse forFailure(String url, Throwable fail, OnBitmapListener client) {
        return new BitmapResponse(url, null, fail, client);
    }

    private BitmapResponse(String url, Bitmap bitmap, Throwable fail, OnBitmapListener client) {
        this.url = url;
        this.bitmap = Option.of(bitmap);
        this.fail = fail;
        this.client = client;
    }

    public boolean isFailure() {
        return !bitmap.isPresent();
    }

    public Bitmap getBitmap() {
        return bitmap.get();
    }

    public Throwable getFail() {
        return fail;
    }

    public String getUrl() {
        return url;
    }

    public OnBitmapListener getClient() {
        return client;
    }
}
