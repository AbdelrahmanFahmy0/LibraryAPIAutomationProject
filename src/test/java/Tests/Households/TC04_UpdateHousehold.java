package Tests.Households;

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

import static Models.Households.UpdateHouseholdBody.getUpdateHouseholdBody;
import static Utils.AllureUtils.allureFilter;

@Epic("Households")
@Feature("Update Household")
public class TC04_UpdateHousehold extends TestBase {

    //Variables
    String endpoint = "/households/";
    String householdName = householdData.getJsonData("households[1].name");

    //Methods
    @Test(priority = 1, description = "Check that user can update a household")
    @Severity(SeverityLevel.BLOCKER)
    public void updateHouseholdTC() {
        // Update the household
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .body(getUpdateHouseholdBody(householdName))
                .when()
                .put(endpoint + householdId)
                .then()
                .log().all()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(householdSchema))
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getTime() < 3000);
        Assert.assertEquals(response.jsonPath().getString("name"), householdName);
    }

    @Test(priority = 2, description = "Check that user can't update a household without token")
    @Severity(SeverityLevel.CRITICAL)
    public void updateHouseholdWithoutTokenTC() {
        // Attempt to update the household without a token
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .body(getUpdateHouseholdBody(householdName))
                .when()
                .put(endpoint + householdId)
                .then()
                .log().all()
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 403);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 2, description = "Check that user can't update a household with invalid token")
    @Severity(SeverityLevel.CRITICAL)
    public void updateHouseholdWithInvalidTokenTC() {
        // Attempt to update the household with an invalid token
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", invalidToken)
                .body(getUpdateHouseholdBody(householdName))
                .when()
                .put(endpoint + householdId)
                .then()
                .log().all()
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 403);
        Assert.assertTrue(response.getTime() < 3000);
    }
}
