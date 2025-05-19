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

import java.util.Map;

import static Utils.AllureUtils.allureFilter;

@Epic("Users")
@Feature("Retrieve All Users")
public class TC03_RetrieveAllUsers extends TestBase {

    //Variables
    String endpoint = "/users";
    String firstName = userData.getJsonData("users[0].firstName");
    String lastName = userData.getJsonData("users[0].lastName");
    String email = userData.getJsonData("users[0].email");

    //Methods
    @Test(priority = 1, description = "Check that the added user is present in the list of all users")
    @Severity(SeverityLevel.BLOCKER)
    public void checkUserInList() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .when()
                .get(endpoint)
                .then()
                .log().all()
                .extract().response();

        //Find the user with the specified id
        Map<String, Object> user = response.jsonPath().getMap("find { it.id == " + userId + " }");

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getTime() < 3000);
        Assert.assertEquals(user.get("firstName"), firstName);
        Assert.assertEquals(user.get("lastName"), lastName);
        Assert.assertEquals(user.get("email"), email);
    }

    @Test(priority = 2, description = "Check that user can't retrieve all users with invalid token")
    @Severity(SeverityLevel.CRITICAL)
    public void retrieveAllUsersWithInvalidToken() {
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

    @Test(priority = 2, description = "Check that user can't retrieve all users without token")
    @Severity(SeverityLevel.CRITICAL)
    public void retrieveAllUsersWithoutToken() {
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
}
