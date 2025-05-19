package Models.Books;

import org.json.simple.JSONObject;

public class CreateBookBody {

    public static String getCreateBookBody(String title, String author, String isbn, String releaseDate) {
        JSONObject bookBody = new JSONObject();
        bookBody.put("title", title);
        bookBody.put("author", author);
        bookBody.put("isbn", isbn);
        bookBody.put("releaseDate", releaseDate);
        return bookBody.toJSONString();
    }
}
