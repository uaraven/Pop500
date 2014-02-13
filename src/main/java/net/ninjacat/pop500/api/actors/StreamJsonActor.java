package net.ninjacat.pop500.api.actors;

import net.ninjacat.drama.Actor;
import net.ninjacat.drama.ActorRef;
import net.ninjacat.drama.Receiver;
import net.ninjacat.pop500.api.callbacks.OnJsonListener;
import net.ninjacat.pop500.api.messages.JsonResponse;

public class StreamJsonActor extends Actor {

    private final OnJsonListener callback;

    public StreamJsonActor(OnJsonListener callback) {
        this.callback = callback;
    }

    @Receiver
    public void onResponse(ActorRef sender, JsonResponse response) {
        if (response.isFailure()) {
            callback.onJsonFailed(response.getFail());
        } else {
            callback.onJsonReceived(response.getJson());
        }
    }
}
