package net.ninjacat.pop500.api;

import org.json.JSONObject;

/**
 * Created on 21/01/14.
 */
public interface RequestCallback {
    void onSuccess(JSONObject response);

    void onFailure();
}
