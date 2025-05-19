package Tests.Books;

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

@Epic("Books")
@Feature("Delete Book")
public class TC06_DeleteBook extends TestBase {

    //Variables
    String endpoint = "/books/";

    //Methods
    @Test(priority = 1, description = "Check that user can delete a book")
    @Severity(SeverityLevel.BLOCKER)
    public void deleteBookTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Authorization", "Basic " + credentials)
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .when()
                .delete(endpoint + bookId)
                .then()
                .log().all()
                .extract().response();
        //Assertions
        Assert.assertEquals(response.getStatusCode(), 204);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 2, description = "Check that user cannot delete a book without credentials")
    @Severity(SeverityLevel.CRITICAL)
    public void deleteBookWithoutCredentialsTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .when()
                .delete(endpoint + bookId)
                .then()
                .log().all()
                .extract().response();
        //Assertions
        Assert.assertEquals(response.getStatusCode(), 401);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 2, description = "Check that user cannot delete a book with invalid credentials")
    @Severity(SeverityLevel.CRITICAL)
    public void deleteBookWithInvalidCredentialsTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Authorization", "Basic hjkdsfkjdskfj")
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .when()
                .delete(endpoint + bookId)
                .then()
                .log().all()
                .extract().response();
        //Assertions
        Assert.assertEquals(response.getStatusCode(), 401);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 2, description = "Check that user cannot delete a book without token")
    @Severity(SeverityLevel.CRITICAL)
    public void deleteBookWithoutTokenTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Authorization", "Basic " + credentials)
                .header("Content-Type", "application/json")
                .when()
                .delete(endpoint + bookId)
                .then()
                .log().all()
                .extract().response();
        //Assertions
        Assert.assertEquals(response.getStatusCode(), 403);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 2, description = "Check that user cannot delete a book with invalid token")
    @Severity(SeverityLevel.CRITICAL)
    public void deleteBookWithInvalidTokenTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Authorization", "Basic " + credentials)
                .header("Content-Type", "application/json")
                .header("g-token", invalidToken)
                .when()
                .delete(endpoint + bookId)
                .then()
                .log().all()
                .extract().response();
        //Assertions
        Assert.assertEquals(response.getStatusCode(), 403);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 3, description = "Check that user cannot delete a book with invalid ID")
    public void deleteBookWithInvalidIDTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Authorization", "Basic " + credentials)
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .when()
                .delete(endpoint + invalidBookId)
                .then()
                .log().all()
                .extract().response();
        //Assertions
        Assert.assertEquals(response.getStatusCode(), 404);
        Assert.assertTrue(response.getTime() < 3000);
        Assert.assertEquals(response.jsonPath().getString("message"), "Book not found");
    }
}
