package net.ninjacat.pop500.api.messages;

import net.ninjacat.pop500.api.callbacks.OnJsonListener;
import org.json.JSONObject;

public final class JsonResponse {
    private final int pageNumber;
    private final JSONObject json;
    private final OnJsonListener listener;
    private final Throwable fail;

    public static JsonResponse forSuccess(int pageNumber, JSONObject json, OnJsonListener listener) {
        return new JsonResponse(pageNumber, json, listener, null);
    }

    public static JsonResponse forFailure(int pageNumber, Throwable fail, OnJsonListener listener) {
        return new JsonResponse(pageNumber, null, listener, fail);
    }

    private JsonResponse(int pageNumber, JSONObject json, OnJsonListener listener, Throwable fail) {
        this.pageNumber = pageNumber;
        this.json = json;
        this.listener = listener;
        this.fail = fail;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public OnJsonListener getListener() {
        return listener;
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
