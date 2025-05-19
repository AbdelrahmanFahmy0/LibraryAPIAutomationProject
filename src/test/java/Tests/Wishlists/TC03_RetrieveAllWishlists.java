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

import java.util.Map;

import static Utils.AllureUtils.allureFilter;

@Epic("Wishlists")
@Feature("Retrieve All Wishlists")
public class TC03_RetrieveAllWishlists extends TestBase {

    //Variables
    String endpoint = "/wishlists";
    String wishlistName = wishlistData.getJsonData("wishlist[0].name");

    //Methods
    @Test(priority = 1, description = "Check that the added wishlist is present in the list of all wishlists")
    @Severity(SeverityLevel.BLOCKER)
    public void retrieveAllWishlistsTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .when()
                .get(endpoint)
                .then()
                .log().all()
                .extract().response();

        //Find the wishlist with the specified id
        Map<String, Object> wishlist = response.jsonPath().getMap("find { it.id == " + wishlistId + " }");

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getTime() < 3000);
        Assert.assertEquals(wishlist.get("name"), wishlistName);
    }

    @Test(priority = 2, description = "Check that user cannot retrieve all wishlists without token")
    @Severity(SeverityLevel.CRITICAL)
    public void retrieveAllWishlistsWithoutTokenTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .when()
                .get(endpoint)
                .then()
                .log().all()
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 403);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 2, description = "Check that user cannot retrieve all wishlists with invalid token")
    @Severity(SeverityLevel.CRITICAL)
    public void retrieveAllWishlistsWithInvalidTokenTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", invalidToken)
                .when()
                .get(endpoint)
                .then()
                .log().all()
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 403);
        Assert.assertTrue(response.getTime() < 3000);
    }
}
