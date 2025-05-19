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

import static Models.Users.CreateUserBody.getCreateUserBody;
import static Utils.AllureUtils.allureFilter;

@Epic("Users")
@Feature("Create User")
public class TC01_CreateUser extends TestBase {

    //Variables
    String endpoint = "/users";
    String firstName = userData.getJsonData("users[0].firstName");
    String lastName = userData.getJsonData("users[0].lastName");
    String email = userData.getJsonData("users[0].email");

    //Methods
    @Test(priority = 1, description = "Check that user is created successfully")
    @Severity(SeverityLevel.BLOCKER)
    public void createUserTC() {
        // Create a new user
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .body(getCreateUserBody(firstName, lastName, email))
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(userSchema))
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 201);
        Assert.assertTrue(response.getTime() < 3000);
        Assert.assertEquals(response.jsonPath().getString("firstName"), firstName);
        Assert.assertEquals(response.jsonPath().getString("lastName"), lastName);
        Assert.assertEquals(response.jsonPath().getString("email"), email);

        //Set User ID
        userId = response.jsonPath().getInt("id");
    }

    @Test(priority = 2, description = "Check that user is not created with invalid token")
    @Severity(SeverityLevel.CRITICAL)
    public void createUserWithInvalidTokenTC() {
        // Create a new user with invalid token
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", invalidToken)
                .body(getCreateUserBody(firstName, lastName, email))
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .extract().response();

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 403);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 2, description = "Check that user is not created without token")
    @Severity(SeverityLevel.CRITICAL)
    public void createUserWithoutTokenTC() {
        // Create a new user without token
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .body(getCreateUserBody(firstName, lastName, email))
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
