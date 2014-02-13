package net.ninjacat.pop500.api.messages;

import net.ninjacat.drama.ActorRef;
import org.json.JSONObject;

public final class JsonRequest {
    private final int pageNumber;
    private final String url;
    private final ActorRef client;

    public JsonRequest(int pageNumber, String url, ActorRef client) {
        this.pageNumber = pageNumber;
        this.url = url;
        this.client = client;
    }

    public String getUrl() {
        return url;
    }

    public JsonResponse createResponse(JSONObject content) {
        return JsonResponse.forSuccess(pageNumber, content, client);
    }

    public JsonResponse createResponse(Throwable ex) {
        return JsonResponse.forFailure(pageNumber, ex, client);
    }
}
