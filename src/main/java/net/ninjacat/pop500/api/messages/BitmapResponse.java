package net.ninjacat.pop500.api.messages;

import android.graphics.Bitmap;
import net.ninjacat.drama.ActorRef;
import net.ninjacat.drama.Option;

public final class BitmapResponse {
    private final String url;
    private final Option<Bitmap> bitmap;
    private final Throwable fail;
    private final ActorRef client;

    public static BitmapResponse forSuccess(String url, Bitmap bitmap, ActorRef originalSender) {
        return new BitmapResponse(url, bitmap, null, originalSender);
    }

    public static BitmapResponse forFailure(String url, Throwable fail, ActorRef originalSender) {
        return new BitmapResponse(url, null, fail, originalSender);
    }

    private BitmapResponse(String url, Bitmap bitmap, Throwable fail, ActorRef client) {
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

    public ActorRef getClient() {
        return client;
    }
}
