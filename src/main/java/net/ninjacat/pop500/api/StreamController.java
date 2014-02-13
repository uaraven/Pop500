package net.ninjacat.pop500.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.Toast;
import com.google.common.base.Optional;
import net.ninjacat.drama.ActorRef;
import net.ninjacat.drama.ActorSystem;
import net.ninjacat.pop500.R;
import net.ninjacat.pop500.api.actors.StreamBitmapActor;
import net.ninjacat.pop500.api.actors.StreamJsonActor;
import net.ninjacat.pop500.api.callbacks.OnBitmapListener;
import net.ninjacat.pop500.api.callbacks.OnJsonListener;
import net.ninjacat.pop500.api.images.ImageCache;
import net.ninjacat.pop500.logger.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class StreamController {

    private static final String STREAM_JSON = "StreamJson";
    private static final String STREAM_BITMAP = "StreamBitmap";
    private static final int READ_AHEAD_THRESHOLD = 100;

    private final Api500Px api;
    private final PhotoCache cache;
    private final ImageCache imageCache;
    private final Context context;
    private final Handler handler;
    private final List<Integer> photoIds;
    private final ActorRef streamJsonActor;
    private final ActorRef streamBitmapActor;
    private Optional<StreamUpdateListener> updateListener;

    @Inject
    public StreamController(Api500Px api,
                            PhotoCache cache,
                            ImageCache imageCache,
                            ActorSystem actorSystem,
                            Context context,
                            Handler handler) {
        this.api = api;
        this.cache = cache;
        this.imageCache = imageCache;
        this.context = context;
        this.handler = handler;
        this.photoIds = new ArrayList<Integer>(1000);

        this.updateListener = Optional.absent();

        streamJsonActor = actorSystem.createActor(StreamJsonActor.class, STREAM_JSON, new StreamJsonCallback());
        streamBitmapActor = actorSystem.createActor(StreamBitmapActor.class, STREAM_BITMAP, new StreamBitmapCallback());
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

    public void reset() {
        cache.evictAll();
        photoIds.clear();
        notifyStreamUpdate();
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
        api.getImage(photo.getUrl(), streamBitmapActor);
    }

    private void prefetchPageIfNeeded(int index) {
        if (photoIds.size() - READ_AHEAD_THRESHOLD < index) {
            retrieveMissingPage(photoIds.size() + 1);
        }
    }

    private void retrieveMissingPage(int index) {
        int pageNo = index / Api500Px.PAGE_SIZE + 1;
        Logger.debug("[StreamController] Trying to get page %d", pageNo);

        api.getPopularPhotos(pageNo, streamJsonActor);
    }

    private void processListOfPhotos(JSONObject result) {
        try {
            if (result.has("error")) {
                showFailureMessage(result.getString("error"));
            }

            JSONArray photoArray = result.getJSONArray("photos");

            int length = photoArray.length();
            for (int i = 0; i < length; i++) {
                storePhoto(photoArray.getJSONObject(i));
            }

            notifyStreamUpdate();
        } catch (JSONException e) {
            Logger.error("[StreamController] Response does not contain photos", e);
            Logger.debug("[StreamController] JSON: %s", result);
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

    private void showFailureMessage(final int messageResourceId) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, messageResourceId, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showFailureMessage(final String message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class StreamJsonCallback implements OnJsonListener {
        @Override
        public void onJsonReceived(JSONObject result) {
            processListOfPhotos(result);
        }

        @Override
        public void onJsonFailed(Throwable fail) {
            Logger.error("[StreamController] Failed to load stream chunk", fail);
            showFailureMessage(R.string.json_failed);
        }
    }

    private class StreamBitmapCallback implements OnBitmapListener {

        @Override
        public void bitmapAvailable(String key, Bitmap bitmap) {
            Logger.debug("[StreamController] Bitmap loaded from %s", key);
            imageCache.storeBitmap(key, bitmap);
            notifyStreamUpdate();
        }

        @Override
        public void bitmapFailed(Throwable fail) {
            showFailureMessage(R.string.image_failed);
            Logger.error("[StreamController] Failed to fetch bitmap", fail);
        }
    }
}
