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

import static Models.Households.CreateHouseholdBody.getCreateHouseholdBody;
import static Utils.AllureUtils.allureFilter;

@Epic("Households")
@Feature("Create Household")
public class TC01_CreateHousehold extends TestBase {

    //Variables
    String endpoint = "/households";
    String householdName = householdData.getJsonData("households[0].name");

    //Methods
    @Test(priority = 1, description = "Check that user can create a new household")
    @Severity(SeverityLevel.BLOCKER)
    public void createHouseholdTC() {
        // Create a new household
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .body(getCreateHouseholdBody(householdName))
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(householdSchema))
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 201);
        Assert.assertTrue(response.getTime() < 3000);
        Assert.assertEquals(response.jsonPath().getString("name"), householdName);

        //Set Household ID
        householdId = response.jsonPath().getInt("id");
    }

    @Test(priority = 2, description = "Check that user can't create a new household without token")
    @Severity(SeverityLevel.CRITICAL)
    public void createHouseholdWithoutTokenTC() {
        // Attempt to create a new household without a token
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .body(getCreateHouseholdBody(householdName))
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 403);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 2, description = "Check that user can't create a new household with invalid token")
    @Severity(SeverityLevel.CRITICAL)
    public void createHouseholdWithInvalidTokenTC() {
        // Attempt to create a new household with an invalid token
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", invalidToken)
                .body(getCreateHouseholdBody(householdName))
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
