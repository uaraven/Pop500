package net.ninjacat.pop500.api.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import net.ninjacat.pop500.api.callbacks.OnBitmapListener;
import net.ninjacat.pop500.logger.Logger;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * User: raven
 * Date: 05.10.12
 */
public class BitmapDownloader implements Runnable {

    private final String source;
    private final OnBitmapListener listener;

    public BitmapDownloader(String source, OnBitmapListener listener) {
        assert listener != null;
        this.source = source;
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            Logger.debug("[BitmapDownloader] Fetching bitmap for %s", source);
            URL url = new URL(source);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            BitmapFactory.Options op = getDefaultBitmapOptions();
            Bitmap result = BitmapFactory.decodeStream(connection.getInputStream(), new Rect(0, 0, 0, 0), op);
            connection.disconnect();
            listener.bitmapAvailable(result);
        } catch (Exception e) {
            Logger.debug("[BitmapDownloader] Failed to fetch bitmap for " + source);
            listener.bitmapFailed(e);
        }
    }

    private BitmapFactory.Options getDefaultBitmapOptions() {
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inPreferredConfig = Bitmap.Config.RGB_565;
        return op;
    }

}
