package net.ninjacat.pop500.api.net;

public class Access {
    private static final String API_KEY = "NzdseD03bWiGOFKxKeFjlYOmtsnI1qn1JSFdZOkJ";

    private static final String BASE_URL = "https://api.500px.com/v1/photos";

    private static final String QUERY = "?feature=popular&image_size=%d&sort=%s&page=%d&rpp=%d&consumer_key=%s";

    private static final int IMAGE_SIZE = 4;
    private static final String SORT_ORDER = "rating";

    public static String getPage(int pageNo, int photosPerPage) {
        return BASE_URL + String.format(QUERY, IMAGE_SIZE, SORT_ORDER, pageNo, photosPerPage, API_KEY);
    }
}
