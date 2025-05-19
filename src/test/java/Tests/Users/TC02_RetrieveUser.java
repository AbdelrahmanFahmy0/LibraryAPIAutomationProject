package Tests.Users;

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

@Epic("Users")
@Feature("Retrieve User")
public class TC02_RetrieveUser extends TestBase {

    //Variables
    String endpoint = "/users/";
    String firstName = userData.getJsonData("users[0].firstName");
    String lastName = userData.getJsonData("users[0].lastName");
    String email = userData.getJsonData("users[0].email");
    String updatedFirstName = userData.getJsonData("users[1].firstName");
    String updatedLastName = userData.getJsonData("users[1].lastName");
    String updatedEmail = userData.getJsonData("users[1].email");
    String partiallyUpdatedEmail = userData.getJsonData("users[2].email");

    //Methods
    @Test(priority = 1, description = "Check that user is added successfully")
    @Severity(SeverityLevel.BLOCKER)
    public void retrieveUserTC() {
        // Retrieve the user
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .when()
                .get(endpoint + userId)
                .then()
                .log().all()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(userSchema))
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getTime() < 3000);
        Assert.assertEquals(response.jsonPath().getString("firstName"), firstName);
        Assert.assertEquals(response.jsonPath().getString("lastName"), lastName);
        Assert.assertEquals(response.jsonPath().getString("email"), email);
    }

    @Test(priority = 2, description = "Check that user is not retrieved with invalid token")
    @Severity(SeverityLevel.CRITICAL)
    public void retrieveUserWithInvalidTokenTC() {
        // Retrieve the user with invalid token
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", invalidToken)
                .when()
                .get(endpoint + userId)
                .then()
                .log().all()
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 403);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 2, description = "Check that user is not retrieved without token")
    @Severity(SeverityLevel.CRITICAL)
    public void retrieveUserWithoutTokenTC() {
        // Retrieve the user without token
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .when()
                .get(endpoint + userId)
                .then()
                .log().all()
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 403);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 3, description = "Check that user is not retrieved with invalid user ID")
    @Severity(SeverityLevel.CRITICAL)
    public void retrieveUserWithInvalidUserIdTC() {
        // Retrieve the user with invalid user ID
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .when()
                .get(endpoint + invalidUserId)
                .then()
                .log().all()
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 404);
        Assert.assertTrue(response.getTime() < 3000);
        Assert.assertEquals(response.jsonPath().getString("message"), "User not found");
    }

    @Test(priority = 4, description = "Check that user is updated successfully")
    @Severity(SeverityLevel.CRITICAL)
    public void retrieveUpdatedUserTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .when()
                .get(endpoint + userId)
                .then()
                .log().all()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(userSchema))
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getTime() < 3000);
        Assert.assertEquals(response.jsonPath().getString("firstName"), updatedFirstName);
        Assert.assertEquals(response.jsonPath().getString("lastName"), updatedLastName);
        Assert.assertEquals(response.jsonPath().getString("email"), updatedEmail);
    }

    @Test(priority = 5, description = "Check that user is partially updated successfully")
    @Severity(SeverityLevel.CRITICAL)
    public void retrievePartiallyUpdatedUserTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .when()
                .get(endpoint + userId)
                .then()
                .log().all()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(userSchema))
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getTime() < 3000);
        Assert.assertEquals(response.jsonPath().getString("email"), partiallyUpdatedEmail);
    }

    @Test(priority = 6, description = "Check that user is deleted successfully")
    @Severity(SeverityLevel.CRITICAL)
    public void retrieveDeletedUserTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .when()
                .get(endpoint + userId)
                .then()
                .log().all()
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 404);
        Assert.assertTrue(response.getTime() < 3000);
        Assert.assertEquals(response.jsonPath().getString("message"), "User not found");
    }
}
