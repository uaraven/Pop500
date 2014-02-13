package net.ninjacat.pop500.api.messages;

import net.ninjacat.pop500.api.callbacks.OnJsonListener;
import org.json.JSONObject;

public final class JsonRequest {
    private final int pageNumber;
    private final String url;
    private final OnJsonListener client;

    public JsonRequest(int pageNumber, String url, OnJsonListener client) {
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
