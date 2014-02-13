package net.ninjacat.pop500.api.messages;

import net.ninjacat.pop500.api.callbacks.OnJsonListener;
import org.json.JSONObject;

public final class JsonResponse {
    private final int pageNumber;
    private final JSONObject json;
    private final OnJsonListener client;
    private final Throwable fail;

    private JsonResponse(int pageNumber, JSONObject json, OnJsonListener client, Throwable fail) {
        this.pageNumber = pageNumber;
        this.json = json;
        this.client = client;
        this.fail = fail;
    }

    public static JsonResponse forSuccess(int pageNumber, JSONObject json, OnJsonListener client) {
        return new JsonResponse(pageNumber, json, client, null);
    }

    public static JsonResponse forFailure(int pageNumber, Throwable fail, OnJsonListener client) {
        return new JsonResponse(pageNumber, null, client, fail);
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public OnJsonListener getClient() {
        return client;
    }

    public JSONObject getJson() {
        return json;
    }

    public Throwable getFail() {
        return fail;
    }

    public boolean isFailure() {
        return fail != null;
    }
}
