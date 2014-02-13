package net.ninjacat.pop500.api.actors;

import net.ninjacat.drama.Actor;
import net.ninjacat.drama.ActorRef;
import net.ninjacat.drama.Receiver;
import net.ninjacat.pop500.api.messages.BitmapResponse;

import java.util.Set;

public class BitmapApiResponseActor extends Actor {

    private final Set<String> bitmapsInProgress;

    public BitmapApiResponseActor(Set<String> bitmapsInProgress) {
        this.bitmapsInProgress = bitmapsInProgress;
    }

    @Receiver
    public void onResponse(ActorRef sender, BitmapResponse response) {
        bitmapsInProgress.remove(response.getUrl());
        response.getClient().tell(response);
    }
}
