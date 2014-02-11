package net.ninjacat.pop500.api.callbacks;

import org.json.JSONObject;

public interface OnJsonListener {
    void onSuccess(JSONObject result);

    void onFailure(Throwable fail);
}
