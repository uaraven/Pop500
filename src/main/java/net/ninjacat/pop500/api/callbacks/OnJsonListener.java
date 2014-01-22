package net.ninjacat.pop500.api.callbacks;

import org.json.JSONObject;

/**
 * User: raven
 * Date: 05.10.12
 */
public interface OnJsonListener {
    void onSuccess(JSONObject result);

    void onFailure(Exception fail);
}