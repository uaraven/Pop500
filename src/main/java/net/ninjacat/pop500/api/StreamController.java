package net.ninjacat.pop500.api;

import android.graphics.Bitmap;
import com.google.common.base.Optional;
import net.ninjacat.pop500.api.callbacks.OnBitmapListener;
import net.ninjacat.pop500.api.callbacks.OnJsonListener;
import net.ninjacat.pop500.api.images.ImageCache;
import net.ninjacat.pop500.api.images.ImageRetriever;
import net.ninjacat.pop500.logger.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 21/01/14.
 */
public class StreamController {

    public static final OnBitmapListener DUMMY_LISTENER = new OnBitmapListener() {
        @Override
        public void bitmapAvailable(Bitmap bitmap) {

        }

        @Override
        public void bitmapFailed(Throwable fail) {

        }
    };
    private static final int READ_AHEAD_THRESHOLD = 100;
    private final Api500Px api;
    private final PhotoCache cache;
    private final ImageCache imageCache;
    private final ImageRetriever imageRetriever;
    private final List<Integer> photoIds;
    private Optional<StreamUpdateListener> updateListener;

    @Inject
    public StreamController(Api500Px api, PhotoCache cache, ImageCache imageCache, ImageRetriever imageRetriever) {
        this.api = api;
        this.cache = cache;
        this.imageCache = imageCache;
        this.imageRetriever = imageRetriever;
        this.photoIds = new ArrayList<Integer>(1000);

        this.updateListener = Optional.absent();
    }

    public Optional<Photo> getPhoto(int index) {
        prefetchPageIfNeeded(index);
        Optional<Photo> photo = internalGetPhoto(index);
        if (photo.isPresent()) {
            prefetchImages(index);
        }
        return photo;
    }

    public Optional<Bitmap> getImage(Photo photo) {
        if (imageCache.hasBitmap(photo.getUrl())) {
            Bitmap bitmap = imageCache.retrieveBitmap(photo.getUrl());
            return Optional.of(bitmap);
        } else {
            startBitmapRetrieval(photo);
            return Optional.absent();
        }
    }

    public void setListener(StreamUpdateListener streamUpdateListener) {
        updateListener = Optional.of(streamUpdateListener);
    }

    private Optional<Photo> internalGetPhoto(int index) {
        if (index < photoIds.size()) {
            Integer photoId = photoIds.get(index);
            if (cache.hasKey(photoId)) {
                return Optional.of(cache.get(photoId));
            } else {
                retrieveMissingPage(index);
                return Optional.absent();
            }
        } else {
            return Optional.absent();
        }
    }

    private void prefetchImages(int index) {
        for (int i = index; i < index + 10; i++) {
            if (i >= 0 && i < photoIds.size()) {
                Integer photoId = photoIds.get(i);
                if (cache.hasKey(photoId) && !imageCache.hasBitmap(cache.get(photoId).getUrl())) {
                    Photo photo = cache.get(photoId);
                    Logger.debug("[StreamController] Prefetching bitmap %s", photo.getId());
                    startBitmapRetrieval(photo);
                }
            }
        }
    }

    private void startBitmapRetrieval(Photo photo) {
        imageRetriever.retrieveBitmap(photo.getUrl(), new BitmapDownloadCallback(photo.getUrl()));
    }

    private void prefetchPageIfNeeded(int index) {
        if (photoIds.size() - READ_AHEAD_THRESHOLD < index) {
            retrieveMissingPage(photoIds.size() + 1);
        }
    }

    private void retrieveMissingPage(int index) {
        int pageNo = index / Api500Px.PAGE_SIZE + 1;
        Logger.debug("[StreamController] Trying to get page %d", pageNo);

        api.getPopularPhotos(pageNo, new JsonCallback());
    }

    private void processListOfPhotos(JSONObject result) {
        try {
            JSONArray photoArray = result.getJSONArray("photos");

            int length = photoArray.length();
            for (int i = 0; i < length; i++) {
                storePhoto(photoArray.getJSONObject(i));
            }

            notifyStreamUpdate();
        } catch (JSONException e) {
            Logger.error("[StreamController] Response does not contain photos", e);
        }
    }

    private void notifyStreamUpdate() {
        if (updateListener.isPresent()) {
            updateListener.get().onStreamUpdated();
        }
    }

    private void storePhoto(JSONObject photoJson) throws JSONException {
        Photo photo = Photo.parse(photoJson);
        cache.put(photo.getId(), photo);
        photoIds.add(photo.getId());
    }

    private class JsonCallback implements OnJsonListener {
        @Override
        public void onSuccess(JSONObject result) {
            processListOfPhotos(result);
        }

        @Override
        public void onFailure(Exception fail) {
            Logger.error("[StreamController] Failed to load stream chunk", fail);
        }
    }

    private class BitmapDownloadCallback implements OnBitmapListener {

        private final String key;

        public BitmapDownloadCallback(String key) {
            this.key = key;
        }

        @Override
        public void bitmapAvailable(Bitmap bitmap) {
            Logger.debug("[StreamController] Bitmap loaded from %s", key);
            imageCache.storeBitmap(key, bitmap);
            notifyStreamUpdate();
        }

        @Override
        public void bitmapFailed(Throwable fail) {
            Logger.error("[StreamController] Failed to fetch bitmap from " + key, fail);
        }
    }
}
