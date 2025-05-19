package Tests.Wishlists;

import Tests.Base.TestBase;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static Utils.AllureUtils.allureFilter;

@Epic("Wishlists")
@Feature("Delete Wishlist")
public class TC06_DeleteWishlist extends TestBase {

    //Variables
    String endpoint = "/wishlists/";

    //Methods
    @Test(priority = 1, description = "Check that user can delete a wishlist")
    @Severity(SeverityLevel.BLOCKER)
    public void deleteWishlistTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .when()
                .delete(endpoint + wishlistId)
                .then()
                .log().all()
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 204);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 2, description = "Check that user cannot delete a wishlist without token")
    @Severity(SeverityLevel.CRITICAL)
    public void deleteWishlistWithoutTokenTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .when()
                .delete(endpoint + wishlistId)
                .then()
                .log().all()
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 403);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 2, description = "Check that user cannot delete a wishlist with invalid token")
    @Severity(SeverityLevel.CRITICAL)
    public void deleteWishlistWithInvalidTokenTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Authorization", "Basic " + credentials)
                .header("Content-Type", "application/json")
                .header("g-token", invalidToken)
                .when()
                .delete(endpoint + wishlistId)
                .then()
                .log().all()
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 403);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 3, description = "Check that user cannot delete a wishlist with invalid ID")
    public void deleteWishlistWithInvalidIdTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Authorization", "Basic " + credentials)
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .when()
                .delete(endpoint + invalidWishlistId)
                .then()
                .log().all()
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 404);
        Assert.assertTrue(response.getTime() < 3000);
        Assert.assertEquals(response.jsonPath().getString("message"), "Wishlist not found");
    }
}
