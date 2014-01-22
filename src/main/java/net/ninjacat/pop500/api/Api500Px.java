package net.ninjacat.pop500.api;

import android.graphics.Bitmap;
import net.ninjacat.pop500.api.callbacks.OnBitmapListener;
import net.ninjacat.pop500.api.callbacks.OnJsonListener;
import net.ninjacat.pop500.api.net.Access;
import net.ninjacat.pop500.api.net.BitmapDownloader;
import net.ninjacat.pop500.api.net.JsonDownloader;
import net.ninjacat.pop500.logger.Logger;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * Created on 21/01/14.
 */
public class Api500Px {
    public static final int PAGE_SIZE = 40;
    private Set<Integer> pagesInProgress;
    private Set<String> bitmapsInProgress;
    private ExecutorService executorService;
    private final ExecutorService jsonExecutorService;

    @Inject
    public Api500Px(ExecutorService executorService, @Queued ExecutorService jsonExecutorService) {
        this.executorService = executorService;
        this.jsonExecutorService = jsonExecutorService;
        this.pagesInProgress = Collections.synchronizedSet(new HashSet<Integer>());
        this.bitmapsInProgress = Collections.synchronizedSet(new HashSet<String>());
    }

    public void getPopularPhotos(final int pageNumber, final OnJsonListener callback) {
        if (pagesInProgress.contains(pageNumber)) {
            Logger.debug("[Api500px] Page %d already in download queue", pageNumber);
        } else {
            pagesInProgress.add(pageNumber);
            jsonExecutorService.submit(new JsonDownloader(Access.getPage(pageNumber, PAGE_SIZE),
                    new JsonDownloadListener(pageNumber, callback)));
        }
    }

    public void getImage(String url, OnBitmapListener callback) {
        if (bitmapsInProgress.contains(url)) {
            Logger.debug("[Api500px] Bitmap %s is already in queue", url);
        } else {
            Logger.debug("[Api500px] Starting images retrieval from %s", url);
            bitmapsInProgress.add(url);
            executorService.submit(new BitmapDownloader(url, new BitmapDownloadListener(url, callback)));
        }
    }

    private class JsonDownloadListener implements OnJsonListener {
        private final int pageNumber;
        private final OnJsonListener chainedListener;

        private JsonDownloadListener(int pageNumber, OnJsonListener chainedListener) {
            this.pageNumber = pageNumber;
            this.chainedListener = chainedListener;
        }

        @Override
        public void onSuccess(JSONObject result) {
            pagesInProgress.remove(pageNumber);
            chainedListener.onSuccess(result);
        }

        @Override
        public void onFailure(Exception fail) {
            pagesInProgress.remove(pageNumber);
            chainedListener.onFailure(fail);
        }
    }

    private class BitmapDownloadListener implements OnBitmapListener {

        private final String key;
        private final OnBitmapListener chainedListener;

        private BitmapDownloadListener(String key, OnBitmapListener chainedListener) {
            this.key = key;
            this.chainedListener = chainedListener;
        }

        @Override
        public void bitmapAvailable(Bitmap bitmap) {
            bitmapsInProgress.remove(key);
            chainedListener.bitmapAvailable(bitmap);
        }

        @Override
        public void bitmapFailed(Throwable fail) {
            bitmapsInProgress.remove(key);
            chainedListener.bitmapFailed(fail);
        }
    }
}
