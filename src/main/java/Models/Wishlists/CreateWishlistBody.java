package Models.Wishlists;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class CreateWishlistBody {

    public static String getCreateWishlistBody(String name, int bookId) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", name);
        JSONArray bookIds = new JSONArray();
        bookIds.add(bookId);
        requestBody.put("books", bookIds);
        return requestBody.toJSONString();
    }
}
