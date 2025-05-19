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

import static Models.Users.UpdateUserBody.getUpdateUserBody;
import static Utils.AllureUtils.allureFilter;

@Epic("Users")
@Feature("Update User")
public class TC04_UpdateUser extends TestBase {

    //Variables
    String endpoint = "/users/";
    String firstName = userData.getJsonData("users[1].firstName");
    String lastName = userData.getJsonData("users[1].lastName");
    String email = userData.getJsonData("users[1].email");

    //Methods
    @Test(priority = 1, description = "Check that user can be updated successfully")
    @Severity(SeverityLevel.BLOCKER)
    public void updateUserTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .body(getUpdateUserBody(firstName, lastName, email))
                .when()
                .put(endpoint + userId)
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

    @Test(priority = 2, description = "Check that user is not updated with invalid token")
    @Severity(SeverityLevel.CRITICAL)
    public void updateUserWithInvalidTokenTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", invalidToken)
                .body(getUpdateUserBody(firstName, lastName, email))
                .when()
                .put(endpoint + userId)
                .then()
                .log().all()
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 403);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 2, description = "Check that user is not updated without token")
    @Severity(SeverityLevel.CRITICAL)
    public void updateUserWithoutTokenTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .body(getUpdateUserBody(firstName, lastName, email))
                .when()
                .put(endpoint + userId)
                .then()
                .log().all()
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 403);
        Assert.assertTrue(response.getTime() < 3000);
    }
}
