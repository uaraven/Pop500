package net.ninjacat.pop500.api;

import net.ninjacat.drama.ActorRef;
import net.ninjacat.drama.ActorSystem;
import net.ninjacat.pop500.api.actors.BitmapApiResponseActor;
import net.ninjacat.pop500.api.actors.JsonApiResponseActor;
import net.ninjacat.pop500.api.callbacks.OnBitmapListener;
import net.ninjacat.pop500.api.callbacks.OnJsonListener;
import net.ninjacat.pop500.api.messages.BitmapRequest;
import net.ninjacat.pop500.api.messages.JsonRequest;
import net.ninjacat.pop500.api.net.Access;
import net.ninjacat.pop500.logger.Logger;

import javax.inject.Inject;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Api500Px {
    public static final int PAGE_SIZE = 40;
    private final ActorRef jsonResponder;
    private final ActorRef bitmapResponder;
    private final Set<Integer> pagesInProgress;
    private final Set<String> bitmapsInProgress;
    private final ActorRef jsonService;
    private final ActorRef bitmapService;

    @Inject
    public Api500Px(ActorSystem actorSystem) {
        this.pagesInProgress = Collections.synchronizedSet(new HashSet<Integer>());
        this.bitmapsInProgress = Collections.synchronizedSet(new HashSet<String>());

        jsonService = actorSystem.find("JsonService").get();
        bitmapService = actorSystem.find("BitmapService").get();

        jsonResponder = actorSystem.createActor(JsonApiResponseActor.class, "JsonResponder", pagesInProgress);
        bitmapResponder = actorSystem.createActor(BitmapApiResponseActor.class, "BitmapResponder", bitmapsInProgress);
    }

    public void getPopularPhotos(final int pageNumber, final OnJsonListener caller) {
        if (checkAndAdd(pageNumber, pagesInProgress)) {
            jsonService.tell(createJsonRequest(pageNumber, caller), jsonResponder);
        } else {
            Logger.debug("[Api500px] Page %d already in download queue", pageNumber);
        }
    }

    public void getImage(String url, OnBitmapListener caller) {
        if (checkAndAdd(url, bitmapsInProgress)) {
            Logger.debug("[Api500px] Starting images retrieval from %s", url);
            bitmapService.tell(createBitmapRequest(url, caller), bitmapResponder);
        } else {
            Logger.debug("[Api500px] Bitmap %s is already in queue", url);
        }
    }

    private <T> boolean checkAndAdd(T item, final Set<T> set) {
        synchronized (set) {
            if (!set.contains(item)) {
                set.add(item);
                return true;
            } else {
                return false;
            }
        }
    }

    private BitmapRequest createBitmapRequest(String url, OnBitmapListener caller) {
        return new BitmapRequest(url, caller);
    }

    private JsonRequest createJsonRequest(int pageNumber, OnJsonListener caller) {
        return new JsonRequest(pageNumber, Access.getPage(pageNumber, PAGE_SIZE), caller);
    }

}
