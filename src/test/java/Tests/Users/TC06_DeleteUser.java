package Tests.Users;

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

@Epic("Users")
@Feature("Delete User")
public class TC06_DeleteUser extends TestBase {

    //Variables
    String endpoint = "/users/";

    //Methods
    @Test(priority = 1, description = "Check that user can be deleted")
    @Severity(SeverityLevel.BLOCKER)
    public void deleteUserTC() {
        // Delete the user
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Authorization", "Basic " + credentials)
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .when()
                .delete(endpoint + userId)
                .then()
                .log().all()
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 204);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 2, description = "Check that user cannot be deleted with invalid token")
    @Severity(SeverityLevel.CRITICAL)
    public void deleteUserWithInvalidTokenTC() {
        // Delete the user with invalid token
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Authorization", "Basic " + credentials)
                .header("Content-Type", "application/json")
                .header("g-token", invalidToken)
                .when()
                .delete(endpoint + userId)
                .then()
                .log().all()
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 403);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 2, description = "Check that user cannot be deleted without token")
    @Severity(SeverityLevel.CRITICAL)
    public void deleteUserWithoutTokenTC() {
        // Delete the user without token
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Authorization", "Basic " + credentials)
                .header("Content-Type", "application/json")
                .when()
                .delete(endpoint + userId)
                .then()
                .log().all()
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 403);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 2, description = "Check that user cannot be deleted without credentials")
    @Severity(SeverityLevel.CRITICAL)
    public void deleteUserWithoutCredentialsTC() {
        // Delete the user without credentials
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .when()
                .delete(endpoint + userId)
                .then()
                .log().all()
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 401);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 2, description = "Check that user cannot be deleted with invalid credentials")
    @Severity(SeverityLevel.CRITICAL)
    public void deleteUserWithInvalidCredentialsTC() {
        // Delete the user with invalid credentials
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Authorization", "Basic hjkdsfkjdskfj")
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .when()
                .delete(endpoint + userId)
                .then()
                .log().all()
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 401);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 3, description = "Check that user cannot be deleted with invalid userId")
    public void deleteUserWithInvalidUserIdTC() {
        // Delete the user with invalid userId
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Authorization", "Basic " + credentials)
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .when()
                .delete(endpoint + invalidUserId)
                .then()
                .log().all()
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 404);
        Assert.assertTrue(response.getTime() < 3000);
        Assert.assertEquals(response.jsonPath().getString("message"), "User not found");
    }
}
