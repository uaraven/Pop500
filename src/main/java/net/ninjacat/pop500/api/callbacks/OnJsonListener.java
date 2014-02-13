package net.ninjacat.pop500.api.callbacks;

import org.json.JSONObject;

public interface OnJsonListener {
    void onJsonReceived(JSONObject result);

    void onJsonFailed(Throwable fail);
}
