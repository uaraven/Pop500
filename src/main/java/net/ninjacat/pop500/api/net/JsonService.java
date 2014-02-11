package net.ninjacat.pop500.api.net;

import net.ninjacat.drama.Actor;
import net.ninjacat.drama.ActorRef;
import net.ninjacat.drama.Option;
import net.ninjacat.drama.Receiver;
import net.ninjacat.dws.http.GetMethod;
import net.ninjacat.dws.http.HttpRequest;
import net.ninjacat.dws.http.HttpResponse;
import net.ninjacat.dws.http.TextContent;
import net.ninjacat.dws.internal.RequestFailure;
import net.ninjacat.pop500.api.messages.JsonRequest;
import net.ninjacat.pop500.api.messages.JsonResponse;
import net.ninjacat.pop500.logger.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class JsonService extends Actor {

    @Receiver
    public void onRequest(ActorRef sender, JsonRequest request) {
        Logger.debug("[JsonService] Fetching json for %s", request.getUrl());

        Option<ActorRef> webService = getActorSystem().find("WebService");
        try {
            if (webService.isPresent()) {
                sendRequest(sender, request, webService.get());
            } else {
                Logger.debug("[JsonService] No 'WebService' found");
            }
        } catch (MalformedURLException e) {
            Logger.error("[JsonService] Invalid URL: " + request.getUrl(), e);
        }

    }

    @Receiver
    public void onHttpResponse(ActorRef sender, HttpResponse response) {
        OriginalData original = (OriginalData) response.getSenderData();
        try {
            JSONObject json = getJsonFromResponse(response);

            JsonResponse jsonResponse = original.request.createResponse(json);
            original.sender.tell(jsonResponse, getSelf());

        } catch (Exception e) {
            original.sender.tell(original.request.createResponse(e));
        }
    }

    @Receiver
    public void onFailure(ActorRef sender, RequestFailure failure) {
        OriginalData original = (OriginalData) failure.getSenderData();

        original.sender.tell(original.request.createResponse(failure.getFailure()));
    }

    private JSONObject getJsonFromResponse(HttpResponse response) throws JSONException {
        TextContent content = new TextContent(response.getResponseContent());
        JSONObject json = new JSONObject(content.asString());
        response.close();
        return json;
    }

    private void sendRequest(ActorRef sender, JsonRequest request, ActorRef webService) throws MalformedURLException {
        HttpRequest httpRequest = new HttpRequest(new GetMethod(), new URL(request.getUrl()));
        httpRequest.setSenderData(new OriginalData(sender, request));

        webService.tell(httpRequest, getSelf());
    }

    private static class OriginalData {
        private ActorRef sender;
        private JsonRequest request;

        private OriginalData(ActorRef sender, JsonRequest request) {
            this.sender = sender;
            this.request = request;
        }
    }

}
