package net.ninjacat.pop500.api;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created on 21/01/14.
 */
public class Photo {

    private final int id;
    private final String photoName;
    private final int category;
    private final double rating;
    private final String url;
    private final String userName;

    public Photo(int id, String photoName, int category, double rating, String url, String userName) throws JSONException {

        this.id = id;
        this.photoName = photoName;
        this.category = category;
        this.rating = rating;
        this.url = url;
        this.userName = userName;
    }

    public static Photo parse(JSONObject jsonPhoto) throws JSONException {
        int id = jsonPhoto.getInt("id");
        String photoName = jsonPhoto.getString("name");
        int category = jsonPhoto.getInt("category");
        double rating = jsonPhoto.getDouble("rating");
        String url = jsonPhoto.getString("image_url");
        JSONObject user = jsonPhoto.getJSONObject("user");
        String userName = user.getString("fullname");

        return new Photo(id, photoName, category, rating, url, userName);
    }

    public int getId() {
        return id;
    }

    public String getPhotoName() {
        return photoName;
    }

    public double getRating() {
        return rating;
    }

    public String getUrl() {
        return url;
    }

    public String getUserName() {
        return userName;
    }

    public int getCategory() {
        return category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Photo photo = (Photo) o;

        if (id != photo.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
