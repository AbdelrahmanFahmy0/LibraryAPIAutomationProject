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

import static Models.Wishlists.UpdateWishlistBody.getPartialUpdateWishlistBody;
import static Utils.AllureUtils.allureFilter;

@Epic("Wishlists")
@Feature("Partial Update Wishlist")
public class TC05_PartialUpdateWishlist extends TestBase {

    //Variables
    String endpoint = "/wishlists/";
    String wishlistName = wishlistData.getJsonData("wishlist[2].name");

    //Methods
    @Test(priority = 1, description = "Check that user can partially update a wishlist")
    @Severity(SeverityLevel.BLOCKER)
    public void partialUpdateWishlistTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .body(getPartialUpdateWishlistBody(wishlistName))
                .when()
                .patch(endpoint + wishlistId)
                .then()
                .log().all()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(wishlistSchema))
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getTime() < 3000);
        Assert.assertEquals(response.jsonPath().getString("name"), wishlistName);
    }

    @Test(priority = 2, description = "Check that user cannot partially update a wishlist with invalid token")
    @Severity(SeverityLevel.CRITICAL)
    public void partialUpdateWishlistWithInvalidTokenTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", invalidToken)
                .body(getPartialUpdateWishlistBody(wishlistName))
                .when()
                .patch(endpoint + wishlistId)
                .then()
                .log().all()
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 403);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 2, description = "Check that user cannot partially update a wishlist without token")
    @Severity(SeverityLevel.CRITICAL)
    public void partialUpdateWishlistWithoutTokenTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .body(getPartialUpdateWishlistBody(wishlistName))
                .when()
                .patch(endpoint + wishlistId)
                .then()
                .log().all()
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 403);
        Assert.assertTrue(response.getTime() < 3000);
    }
}
