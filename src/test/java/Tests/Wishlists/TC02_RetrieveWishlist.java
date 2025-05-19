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

import static Utils.AllureUtils.allureFilter;

@Epic("Wishlists")
@Feature("Retrieve Wishlist")
public class TC02_RetrieveWishlist extends TestBase {

    //Variables
    String endpoint = "/wishlists/";
    String wishlistName = wishlistData.getJsonData("wishlist[0].name");
    String updatedWishlistName = wishlistData.getJsonData("wishlist[1].name");
    String partiallyUpdatedWishlistName = wishlistData.getJsonData("wishlist[2].name");

    //Methods
    @Test(priority = 1, description = "Check that user can retrieve a wishlist")
    @Severity(SeverityLevel.BLOCKER)
    public void retrieveWishlistTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .when()
                .get(endpoint + wishlistId)
                .then()
                .log().all()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(wishlistSchema))
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getTime() < 3000);
        Assert.assertEquals(response.jsonPath().getString("name"), wishlistName);
        Assert.assertEquals(response.jsonPath().getInt("books[0]"), bookId);
    }

    @Test(priority = 2, description = "Check that user cannot retrieve a wishlist without token")
    @Severity(SeverityLevel.CRITICAL)
    public void retrieveWishlistWithoutTokenTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .when()
                .get(endpoint + wishlistId)
                .then()
                .log().all()
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 403);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 2, description = "Check that user cannot retrieve a wishlist with invalid token")
    @Severity(SeverityLevel.CRITICAL)
    public void retrieveWishlistWithInvalidTokenTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", invalidToken)
                .when()
                .get(endpoint + wishlistId)
                .then()
                .log().all()
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 403);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 3, description = "Check that user cannot retrieve a wishlist with invalid ID")
    @Severity(SeverityLevel.CRITICAL)
    public void retrieveWishlistWithInvalidIdTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .when()
                .get(endpoint + invalidWishlistId)
                .then()
                .log().all()
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 404);
        Assert.assertTrue(response.getTime() < 3000);
        Assert.assertEquals(response.jsonPath().getString("message"), "Wishlist not found");
    }

    @Test(priority = 4, description = "Check that the wishlist is updated successfully")
    @Severity(SeverityLevel.CRITICAL)
    public void retrieveUpdatedWishlistTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .when()
                .get(endpoint + wishlistId)
                .then()
                .log().all()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(wishlistSchema))
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getTime() < 3000);
        Assert.assertEquals(response.jsonPath().getString("name"), updatedWishlistName);
        Assert.assertEquals(response.jsonPath().getInt("books[0]"), bookId);
    }

    @Test(priority = 5, description = "Check that the wislist is partially updated successfully")
    @Severity(SeverityLevel.CRITICAL)
    public void retrievePartiallyUpdatedWishlistTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .when()
                .get(endpoint + wishlistId)
                .then()
                .log().all()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(wishlistSchema))
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getTime() < 3000);
        Assert.assertEquals(response.jsonPath().getString("name"), partiallyUpdatedWishlistName);
        Assert.assertEquals(response.jsonPath().getInt("books[0]"), bookId);
    }

    @Test(priority = 6, description = "Check that the wishlist is deleted successfully")
    @Severity(SeverityLevel.CRITICAL)
    public void retrieveDeletedWishlistTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .when()
                .get(endpoint + wishlistId)
                .then()
                .log().all()
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 404);
        Assert.assertTrue(response.getTime() < 3000);
        Assert.assertEquals(response.jsonPath().getString("message"), "Wishlist not found");
    }
}