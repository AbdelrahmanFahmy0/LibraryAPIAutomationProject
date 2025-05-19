package Tests.Households;

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

@Epic("Households")
@Feature("Delete Household")
public class TC06_DeleteHousehold extends TestBase {

    //Variables
    String endpoint = "/households/";

    //Methods
    @Test(priority = 1, description = "Check that user can delete a household")
    @Severity(SeverityLevel.BLOCKER)
    public void deleteHousehold() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .when()
                .delete(endpoint + householdId)
                .then()
                .log().all()
                .extract().response();
        //Assertions
        Assert.assertEquals(response.getStatusCode(), 204);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 2, description = "Check that user cannot delete a household without token")
    @Severity(SeverityLevel.CRITICAL)
    public void deleteHouseholdWithoutToken() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .when()
                .delete(endpoint + householdId)
                .then()
                .log().all()
                .extract().response();
        //Assertions
        Assert.assertEquals(response.getStatusCode(), 403);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 2, description = "Check that user cannot delete a household with invalid token")
    @Severity(SeverityLevel.CRITICAL)
    public void deleteHouseholdWithInvalidToken() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", invalidToken)
                .when()
                .delete(endpoint + householdId)
                .then()
                .log().all()
                .extract().response();
        //Assertions
        Assert.assertEquals(response.getStatusCode(), 403);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 3, description = "Check that user cannot delete a household with invalid Id")
    public void deleteHouseholdWithInvalidId() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .when()
                .delete(endpoint + invalidHouseholdId)
                .then()
                .log().all()
                .extract().response();
        //Assertions
        Assert.assertEquals(response.getStatusCode(), 404);
        Assert.assertTrue(response.getTime() < 3000);
        Assert.assertEquals(response.jsonPath().getString("message"), "Household not found");
    }
}
