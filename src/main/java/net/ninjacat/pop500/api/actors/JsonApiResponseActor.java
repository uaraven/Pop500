package net.ninjacat.pop500.api.actors;

import net.ninjacat.drama.Actor;
import net.ninjacat.drama.ActorRef;
import net.ninjacat.drama.Receiver;
import net.ninjacat.pop500.api.messages.JsonResponse;

import java.util.Set;

public class JsonApiResponseActor extends Actor {

    private final Set<Integer> pagesInProgress;

    public JsonApiResponseActor(Set<Integer> pagesInProgress) {
        this.pagesInProgress = pagesInProgress;
    }

    @Receiver
    public void onResponse(ActorRef sender, JsonResponse response) {
        pagesInProgress.remove(response.getPageNumber());
        response.getClient().tell(response);
    }
}
