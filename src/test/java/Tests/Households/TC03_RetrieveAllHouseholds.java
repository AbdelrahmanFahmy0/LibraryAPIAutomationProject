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

import java.util.Map;

import static Utils.AllureUtils.allureFilter;

@Epic("Households")
@Feature("Retrieve All Households")
public class TC03_RetrieveAllHouseholds extends TestBase {

    //Variables
    String endpoint = "/households";
    String householdName = householdData.getJsonData("households[0].name");

    //Methods
    @Test(priority = 1, description = "Check that the added household is present in the list of all households")
    @Severity(SeverityLevel.BLOCKER)
    public void checkHouseholdInList() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .when()
                .get(endpoint)
                .then()
                .log().all()
                .extract().response();

        //Find the household with the specified id
        Map<String, Object> household = response.jsonPath().getMap("find { it.id == " + householdId + " }");

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getTime() < 3000);
        Assert.assertEquals(household.get("name"), householdName);
    }

    @Test(priority = 2, description = "Check that user can't retrieve all households with invalid token")
    @Severity(SeverityLevel.CRITICAL)
    public void retrieveAllHouseholdsWithInvalidToken() {
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

    @Test(priority = 2, description = "Check that user can't retrieve all households without token")
    @Severity(SeverityLevel.CRITICAL)
    public void retrieveAllHouseholdsWithoutToken() {
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
