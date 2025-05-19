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

import static Models.Users.UpdateUserBody.getPartialUpdateUserBody;
import static Utils.AllureUtils.allureFilter;

@Epic("Users")
@Feature("Partial Update User")
public class TC05_PartialUpdateUser extends TestBase {

    //Variables
    String endpoint = "/users/";
    String email = userData.getJsonData("users[2].email");

    //Methods
    @Test(priority = 1, description = "Check that user is partially updated successfully")
    @Severity(SeverityLevel.BLOCKER)
    public void partialUpdateUserTC() {
        // Partial update the user
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .body(getPartialUpdateUserBody(email))
                .when()
                .patch(endpoint + userId)
                .then()
                .log().all()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(userSchema))
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getTime() < 3000);
        Assert.assertEquals(response.jsonPath().getString("email"), email);
    }

    @Test(priority = 2, description = "Check that user is not partially updated with invalid token")
    @Severity(SeverityLevel.CRITICAL)
    public void partialUpdateUserWithInvalidTokenTC() {
        // Partial update the user with invalid token
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", invalidToken)
                .body(getPartialUpdateUserBody(email))
                .when()
                .patch(endpoint + userId)
                .then()
                .log().all()
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 403);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 2, description = "Check that user is not partially updated without token")
    @Severity(SeverityLevel.CRITICAL)
    public void partialUpdateUserWithoutTokenTC() {
        // Partial update the user without token
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .body(getPartialUpdateUserBody(email))
                .when()
                .patch(endpoint + userId)
                .then()
                .log().all()
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 403);
        Assert.assertTrue(response.getTime() < 3000);
    }
}
