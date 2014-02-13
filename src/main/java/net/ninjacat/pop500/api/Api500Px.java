package net.ninjacat.pop500.api;

import net.ninjacat.drama.ActorRef;
import net.ninjacat.drama.ActorSystem;
import net.ninjacat.pop500.api.actors.BitmapApiResponseActor;
import net.ninjacat.pop500.api.actors.JsonApiResponseActor;
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
    private final ActorSystem actorSystem;
    private final ActorRef jsonResponder;
    private final ActorRef bitmapResponder;
    private final Set<Integer> pagesInProgress;
    private final Set<String> bitmapsInProgress;

    @Inject
    public Api500Px(ActorSystem actorSystem) {
        this.actorSystem = actorSystem;
        this.pagesInProgress = Collections.synchronizedSet(new HashSet<Integer>());
        this.bitmapsInProgress = Collections.synchronizedSet(new HashSet<String>());

        jsonResponder = actorSystem.createActor(JsonApiResponseActor.class, "JsonResponder", pagesInProgress);
        bitmapResponder = actorSystem.createActor(BitmapApiResponseActor.class, "BitmapResponder", bitmapsInProgress);
    }

    public void getPopularPhotos(final int pageNumber, final ActorRef caller) {
        if (pagesInProgress.contains(pageNumber)) {
            Logger.debug("[Api500px] Page %d already in download queue", pageNumber);
        } else {
            pagesInProgress.add(pageNumber);
            actorSystem.tryTell("JsonService", createJsonRequest(pageNumber, caller), jsonResponder);
        }
    }

    public void getImage(String url, ActorRef caller) {
        if (bitmapsInProgress.contains(url)) {
            Logger.debug("[Api500px] Bitmap %s is already in queue", url);
        } else {
            Logger.debug("[Api500px] Starting images retrieval from %s", url);
            bitmapsInProgress.add(url);
            actorSystem.tryTell("BitmapService", createBitmapRequest(url, caller), bitmapResponder);
        }
    }

    private BitmapRequest createBitmapRequest(String url, ActorRef caller) {
        return new BitmapRequest(url, caller);
    }

    private JsonRequest createJsonRequest(int pageNumber, ActorRef caller) {
        return new JsonRequest(pageNumber, Access.getPage(pageNumber, PAGE_SIZE), caller);
    }

}
