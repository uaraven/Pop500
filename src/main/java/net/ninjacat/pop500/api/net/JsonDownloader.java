package net.ninjacat.pop500.api.net;

import net.ninjacat.pop500.api.callbacks.OnJsonListener;
import net.ninjacat.pop500.logger.Logger;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * User: raven
 * Date: 05.10.12
 */
public class JsonDownloader implements Runnable {

    private final String source;
    private final OnJsonListener listener;

    public JsonDownloader(String source, OnJsonListener listener) {
        this.source = source;
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            Logger.debug("[JsonDownloader] Fetching json for %s", source);
            URL url = new URL(source);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String jsonString = readAll(reader);
            reader.close();
            connection.disconnect();

            JSONObject json = new JSONObject(jsonString);
            listener.onSuccess(json);
        } catch (Exception e) {
            listener.onFailure(e);
        }
    }

    private String readAll(BufferedReader reader) throws IOException {
        String line;
        StringBuilder builder = new StringBuilder();
        line = reader.readLine();
        while (line != null) {
            builder.append(line);
            line = reader.readLine();
        }
        return builder.toString();
    }
}
