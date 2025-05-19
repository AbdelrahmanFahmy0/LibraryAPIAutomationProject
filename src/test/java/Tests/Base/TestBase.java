package Tests.Base;

import Utils.JsonUtils;
import io.restassured.RestAssured;
import Listeners.TestNGListeners;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;

import java.io.File;
import java.util.Base64;

@Listeners(TestNGListeners.class)
public class TestBase {

    //Objects
    public JsonUtils bookData = new JsonUtils("BookData");
    public JsonUtils authData = new JsonUtils("AuthData");
    public JsonUtils householdData = new JsonUtils("HouseholdData");
    public JsonUtils userData = new JsonUtils("UserData");
    public JsonUtils wishlistData = new JsonUtils("WishlistData");
    public File bookSchema = new File("src/test/resources/Schemas/bookSchema.json");
    public File householdSchema = new File("src/test/resources/Schemas/householdSchema.json");
    public File userSchema = new File("src/test/resources/Schemas/userSchema.json");
    public File wishlistSchema = new File("src/test/resources/Schemas/wishlistSchema.json");

    //Variables
    public String token = "ROM831ESV";
    public String invalidToken = "MAL000ESV";
    public static int bookId;
    public int invalidBookId = 99999999;
    public static int householdId;
    public int invalidHouseholdId = 99999999;
    public static int userId;
    public int invalidUserId = 99999999;
    public static int wishlistId;
    public int invalidWishlistId = 99999999;
    String username = authData.getJsonData("validCredentials.username");
    String password = authData.getJsonData("validCredentials.password");
    public String credentials = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());

    //Methods
    @BeforeTest
    public void setBaseURL() {
        // Set up the base URI for the API
        RestAssured.baseURI = "http://localhost:3000";
    }
}
