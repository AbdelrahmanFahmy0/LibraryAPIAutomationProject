package Models.Books;

import org.json.simple.JSONObject;

public class UpdateBookBody {

    //Full update book body
    public static String getUpdateBookBody(String title, String author, String isbn, String releaseDate) {
        JSONObject bookBody = new JSONObject();
        bookBody.put("title", title);
        bookBody.put("author", author);
        bookBody.put("isbn", isbn);
        bookBody.put("releaseDate", releaseDate);
        return bookBody.toJSONString();
    }

    //Partial update book body
    public static String getPartialUpdateBookBody(String title, String author) {
        JSONObject bookBody = new JSONObject();
        bookBody.put("title", title);
        bookBody.put("author", author);
        return bookBody.toJSONString();
    }
}
