package net.ninjacat.pop500.api.images;

import net.ninjacat.pop500.api.Api500Px;
import net.ninjacat.pop500.api.callbacks.OnBitmapListener;

import javax.inject.Inject;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ApiImageRetriever implements ImageRetriever {
    private final Set<String> keysInRetrieving;
    private final Api500Px api;

    @Inject
    protected ApiImageRetriever(Api500Px api) {
        this.api = api;
        this.keysInRetrieving = Collections.synchronizedSet(new HashSet<String>());
    }

    @Override
    public void retrieveBitmap(String key, OnBitmapListener listener) {
        if (isAlreadyRetrieving(key)) {
            return;
        }
        api.getImage(key, listener);
    }

    private boolean isAlreadyRetrieving(String key) {
        return keysInRetrieving.contains(key);
    }


}
