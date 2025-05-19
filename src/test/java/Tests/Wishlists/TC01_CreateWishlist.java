package Tests.Wishlists;

import Tests.Base.TestBase;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static Models.Wishlists.CreateWishlistBody.getCreateWishlistBody;
import static Utils.AllureUtils.allureFilter;

@Epic("Wishlists")
@Feature("Create Wishlist")
public class TC01_CreateWishlist extends TestBase {

    //Variables
    String endpoint = "/wishlists";
    String wishlistName = wishlistData.getJsonData("wishlist[0].name");

    //Methods
    @Test(priority = 1, description = "Check that user can create a wishlist")
    @Severity(SeverityLevel.BLOCKER)
    public void createWishlistTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .body(getCreateWishlistBody(wishlistName, bookId))
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(wishlistSchema))
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 201);
        Assert.assertTrue(response.getTime() < 3000);
        Assert.assertEquals(response.jsonPath().getString("name"), wishlistName);
        Assert.assertEquals(response.jsonPath().getInt("books[0]"), bookId);

        //Set Wishlist ID
        wishlistId = response.jsonPath().getInt("id");
    }

    @Test(priority = 2, description = "Check that user cannot create a wishlist without token")
    @Severity(SeverityLevel.CRITICAL)
    public void createWishlistWithoutTokenTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .body(getCreateWishlistBody(wishlistName, bookId))
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 403);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 2, description = "Check that user cannot create a wishlist with invalid token")
    @Severity(SeverityLevel.CRITICAL)
    public void createWishlistWithInvalidTokenTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", invalidToken)
                .body(getCreateWishlistBody(wishlistName, bookId))
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 403);
        Assert.assertTrue(response.getTime() < 3000);
    }
}
