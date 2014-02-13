package net.ninjacat.pop500.api.actors;

import net.ninjacat.drama.Actor;
import net.ninjacat.drama.ActorRef;
import net.ninjacat.drama.Receiver;
import net.ninjacat.pop500.api.callbacks.OnBitmapListener;
import net.ninjacat.pop500.api.messages.BitmapResponse;

public class StreamBitmapActor extends Actor {

    private final OnBitmapListener callback;

    public StreamBitmapActor(OnBitmapListener callback) {
        this.callback = callback;
    }

    @Receiver
    public void onResponse(ActorRef sender, BitmapResponse response) {
        if (response.isFailure()) {
            callback.bitmapFailed(response.getFail());
        } else {
            callback.bitmapAvailable(response.getUrl(), response.getBitmap());
        }
    }
}
