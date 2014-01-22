package net.ninjacat.pop500.api;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONException;
import org.json.JSONObject;

public class Photo implements Parcelable {

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel parcel) {
            return Photo.fromParcel(parcel);
        }

        @Override
        public Photo[] newArray(int i) {
            return new Photo[i];
        }
    };

    private final int id;
    private final String caption;
    private final double rating;
    private final String url;
    private final String userName;
    private final String description;

    private Photo(int id, String photoName, String description, double rating, String url, String userName) {

        this.id = id;
        this.caption = photoName;
        this.description = description;
        this.rating = rating;
        this.url = url;
        this.userName = userName;
    }

    public static Photo parse(JSONObject jsonPhoto) throws JSONException {
        int id = jsonPhoto.getInt("id");
        String photoName = jsonPhoto.getString("name");
        String description = jsonPhoto.optString("description", "");
        if ("null".equals(description)) {
            description = "";
        }
        double rating = jsonPhoto.getDouble("rating");
        String url = jsonPhoto.getString("image_url");
        JSONObject user = jsonPhoto.getJSONObject("user");
        String userName = user.getString("fullname");

        return new Photo(id, photoName, description, rating, url, userName);
    }

    public static Photo fromParcel(Parcel parcel) {
        int id = parcel.readInt();
        String photoName = parcel.readString();
        String description = parcel.readString();
        double rating = parcel.readDouble();
        String url = parcel.readString();
        String userName = parcel.readString();

        return new Photo(id, photoName, description, rating, url, userName);
    }

    public int getId() {
        return id;
    }

    public String getCaption() {
        return caption;
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

    public String getDescription() {
        return description;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(caption);
        parcel.writeString(description);
        parcel.writeDouble(rating);
        parcel.writeString(url);
        parcel.writeString(userName);
    }

}
