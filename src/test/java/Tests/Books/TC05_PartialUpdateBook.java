package Tests.Books;

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

import static Models.Books.UpdateBookBody.getPartialUpdateBookBody;
import static Utils.AllureUtils.allureFilter;

@Epic("Books")
@Feature("Partial Update Book")
public class TC05_PartialUpdateBook extends TestBase {

    //Variables
    String endpoint = "/books/";
    String author = bookData.getJsonData("books[2].author");
    String title = bookData.getJsonData("books[2].title");

    @Test(priority = 1, description = "Check that user can partially update a book")
    @Severity(SeverityLevel.BLOCKER)
    public void partialUpdateBookTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Authorization", "Basic " + credentials)
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .body(getPartialUpdateBookBody(title, author))
                .when()
                .patch(endpoint + bookId)
                .then()
                .log().all()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(bookSchema))
                .extract().response();
        //Assertions
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getTime() < 3000);
        Assert.assertEquals(response.jsonPath().getString("author"), author);
        Assert.assertEquals(response.jsonPath().getString("title"), title);
    }

    @Test(priority = 2, description = "Check that user cannot partially update a book with invalid credentials")
    @Severity(SeverityLevel.CRITICAL)
    public void partialUpdateBookWithInvalidCredentialsTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Authorization", "Basic hjkdsfkjdskfj")
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .body(getPartialUpdateBookBody(title, author))
                .when()
                .patch(endpoint + bookId)
                .then()
                .log().all()
                .extract().response();
        //Assertions
        Assert.assertEquals(response.getStatusCode(), 401);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 2, description = "Check that user cannot partially update a book without credentials")
    @Severity(SeverityLevel.CRITICAL)
    public void partialUpdateBookWithoutCredentialsTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .body(getPartialUpdateBookBody(title, author))
                .when()
                .patch(endpoint + bookId)
                .then()
                .log().all()
                .extract().response();
        //Assertions
        Assert.assertEquals(response.getStatusCode(), 401);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 2, description = "Check that user cannot partially update a book with invalid token")
    @Severity(SeverityLevel.CRITICAL)
    public void partialUpdateBookWithInvalidTokenTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Authorization", "Basic " + credentials)
                .header("Content-Type", "application/json")
                .header("g-token", invalidToken)
                .body(getPartialUpdateBookBody(title, author))
                .when()
                .patch(endpoint + bookId)
                .then()
                .log().all()
                .extract().response();
        //Assertions
        Assert.assertEquals(response.getStatusCode(), 403);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 2, description = "Check that user cannot partially update a book without token")
    @Severity(SeverityLevel.CRITICAL)
    public void partialUpdateBookWithoutTokenTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Authorization", "Basic " + credentials)
                .header("Content-Type", "application/json")
                .body(getPartialUpdateBookBody(title, author))
                .when()
                .patch(endpoint + bookId)
                .then()
                .log().all()
                .extract().response();
        //Assertions
        Assert.assertEquals(response.getStatusCode(), 403);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 3, description = "Check that user cannot partially update a book with invalid bookId")
    public void partialUpdateBookWithInvalidBookIdTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Authorization", "Basic " + credentials)
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .body(getPartialUpdateBookBody(title, author))
                .when()
                .patch(endpoint + invalidBookId)
                .then()
                .log().all()
                .extract().response();
        //Assertions
        Assert.assertEquals(response.getStatusCode(), 404);
        Assert.assertTrue(response.getTime() < 3000);
    }
}
