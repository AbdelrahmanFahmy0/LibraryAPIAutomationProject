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

import static Utils.AllureUtils.allureFilter;

@Epic("Households")
@Feature("Retrieve Household")
public class TC02_RetrieveHousehold extends TestBase {

    //Variables
    String endpoint = "/households/";
    String householdName = householdData.getJsonData("households[0].name");
    String updatedHouseholdName = householdData.getJsonData("households[1].name");
    String partiallyUpdatedHouseholdName = householdData.getJsonData("households[2].name");

    //Methods
    @Test(priority = 1, description = "Check that user can retrieve a household")
    @Severity(SeverityLevel.BLOCKER)
    public void retrieveHouseholdTC() {
        // Retrieve the household
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .when()
                .get(endpoint + householdId)
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

    @Test(priority = 2, description = "Check that user can't retrieve a household without token")
    @Severity(SeverityLevel.CRITICAL)
    public void retrieveHouseholdWithoutTokenTC() {
        // Attempt to retrieve the household without a token
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .when()
                .get(endpoint + householdId)
                .then()
                .log().all()
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 403);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 2, description = "Check that user can't retrieve a household with invalid token")
    @Severity(SeverityLevel.CRITICAL)
    public void retrieveHouseholdWithInvalidTokenTC() {
        // Attempt to retrieve the household with an invalid token
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", "invalid_token")
                .when()
                .get(endpoint + householdId)
                .then()
                .log().all()
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 403);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 3, description = "Check that user can't retrieve a household with invalid ID")
    public void retrieveHouseholdWithInvalidIDTC() {
        // Attempt to retrieve the household with an invalid ID
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .when()
                .get(endpoint + invalidHouseholdId)
                .then()
                .log().all()
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 404);
        Assert.assertTrue(response.getTime() < 3000);
        Assert.assertEquals(response.jsonPath().getString("message"), "Household not found");
    }

    @Test(priority = 4, description = "Check that the household is updated correctly")
    @Severity(SeverityLevel.CRITICAL)
    public void retrieveUpdatedHouseholdTC() {
        // Retrieve the updated household
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .when()
                .get(endpoint + householdId)
                .then()
                .log().all()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(householdSchema))
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getTime() < 3000);
        Assert.assertEquals(response.jsonPath().getString("name"), updatedHouseholdName);
    }

    @Test(priority = 5, description = "Check that the household is partially updated correctly")
    @Severity(SeverityLevel.CRITICAL)
    public void retrievePartiallyUpdatedHouseholdTC() {
        // Retrieve the partially updated household
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .when()
                .get(endpoint + householdId)
                .then()
                .log().all()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(householdSchema))
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getTime() < 3000);
        Assert.assertEquals(response.jsonPath().getString("name"), partiallyUpdatedHouseholdName);
    }

    @Test(priority = 6, description = "Check that the household is deleted correctly")
    @Severity(SeverityLevel.CRITICAL)
    public void retrieveDeletedHouseholdTC() {
        // Attempt to retrieve the deleted household
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .when()
                .get(endpoint + householdId)
                .then()
                .log().all()
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 404);
        Assert.assertTrue(response.getTime() < 3000);
        Assert.assertEquals(response.jsonPath().getString("message"), "Household not found");
    }
}
