package net.ninjacat.pop500.api.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import net.ninjacat.drama.Actor;
import net.ninjacat.drama.ActorRef;
import net.ninjacat.drama.Option;
import net.ninjacat.drama.Receiver;
import net.ninjacat.dws.WebService;
import net.ninjacat.dws.http.HttpRequest;
import net.ninjacat.dws.http.HttpResponse;
import net.ninjacat.dws.internal.RequestFailure;
import net.ninjacat.pop500.api.messages.BitmapRequest;
import net.ninjacat.pop500.logger.Logger;

import static android.graphics.BitmapFactory.decodeStream;
import static net.ninjacat.dws.RequestBuilder.get;

public class BitmapService extends Actor {

    @Receiver
    public void onBitmapRequest(ActorRef sender, BitmapRequest request) {
        Logger.debug("[BitmapService] Fetching bitmap for %s", request.getUrl());

        Option<ActorRef> webService = getActorSystem().find(WebService.DEFAULT_NAME);
        if (webService.isPresent()) {
            sendRequest(webService.get(), sender, request);
        } else {
            Logger.warn("[BitmapService] No 'WebService' found");
        }
    }

    @Receiver
    public void onSuccess(ActorRef webService, HttpResponse response) {

        OriginalData original = (OriginalData) response.getSenderData();

        try {
            BitmapFactory.Options op = getDefaultBitmapOptions();
            Bitmap bitmap = decodeStream(response.getResponseContent().getContent(), new Rect(0, 0, 0, 0), op);
            response.close();

            original.sender.tell(original.request.createResponse(bitmap));
        } catch (Exception ex) {
            original.sender.tell(original.request.createResponse(ex));
        }
    }

    @Receiver
    public void onFailure(ActorRef sender, RequestFailure failure) {
        OriginalData original = (OriginalData) failure.getSenderData();

        original.sender.tell(original.request.createResponse(failure.getFailure()));
    }

    private void sendRequest(ActorRef webService, ActorRef sender, BitmapRequest bmpRequest) {
        HttpRequest request = get().forUrl(bmpRequest.getUrl()).withLocalData(new OriginalData(sender, bmpRequest)).build();
        webService.tell(request, getSelf());
    }

    private BitmapFactory.Options getDefaultBitmapOptions() {
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inPreferredConfig = Bitmap.Config.RGB_565;
        return op;
    }

    private class OriginalData {
        private ActorRef sender;
        private BitmapRequest request;

        private OriginalData(ActorRef sender, BitmapRequest request) {
            this.sender = sender;
            this.request = request;
        }
    }

}
