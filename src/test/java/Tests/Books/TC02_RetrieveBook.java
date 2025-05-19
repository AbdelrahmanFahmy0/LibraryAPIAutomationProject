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

import static Utils.AllureUtils.allureFilter;

@Epic("Books")
@Feature("Retrieve Book")
public class TC02_RetrieveBook extends TestBase {

    //Variables
    String endpoint = "/books/";
    String author = bookData.getJsonData("books[0].author");
    String title = bookData.getJsonData("books[0].title");
    String isbn = bookData.getJsonData("books[0].isbn");
    String releaseDate = bookData.getJsonData("books[0].releaseDate");
    String updatedAuthor = bookData.getJsonData("books[1].author");
    String updatedTitle = bookData.getJsonData("books[1].title");
    String updatedIsbn = bookData.getJsonData("books[1].isbn");
    String updatedReleaseDate = bookData.getJsonData("books[1].releaseDate");
    String partialUpdatedAuthor = bookData.getJsonData("books[2].author");
    String partialUpdatedTitle = bookData.getJsonData("books[2].title");

    //Methods
    @Test(priority = 1, description = "Check that user can retrieve a book")
    @Severity(SeverityLevel.BLOCKER)
    public void retrieveBookTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .when()
                .get(endpoint + bookId)
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
        Assert.assertEquals(response.jsonPath().getString("isbn"), isbn);
        Assert.assertEquals(response.jsonPath().getString("releaseDate"), releaseDate);
    }

    @Test(priority = 2, description = "Check that user can't retrieve a book without token")
    @Severity(SeverityLevel.CRITICAL)
    public void retrieveBookWithoutTokenTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .when()
                .get(endpoint + bookId)
                .then()
                .log().all()
                .extract().response();
        //Assertions
        Assert.assertEquals(response.getStatusCode(), 403);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 2, description = "Check that user can't retrieve a book with invalid token")
    @Severity(SeverityLevel.CRITICAL)
    public void retrieveBookWithInvalidTokenTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", invalidToken)
                .when()
                .get(endpoint + bookId)
                .then()
                .log().all()
                .extract().response();
        //Assertions
        Assert.assertEquals(response.getStatusCode(), 403);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 3, description = "Check that user can't retrieve a book with invalid ID")
    public void retrieveBookWithInvalidIDTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .when()
                .get(endpoint + invalidBookId)
                .then()
                .log().all()
                .extract().response();
        //Assertions
        Assert.assertEquals(response.getStatusCode(), 404);
        Assert.assertTrue(response.getTime() < 3000);
        Assert.assertEquals(response.jsonPath().getString("message"), "Book not found");
    }

    @Test(priority = 4, description = "Check that the book is updated correctly")
    @Severity(SeverityLevel.CRITICAL)
    public void retrieveUpdatedBookTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .when()
                .get(endpoint + bookId)
                .then()
                .log().all()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(bookSchema))
                .extract().response();
        //Assertions
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getTime() < 3000);
        Assert.assertEquals(response.jsonPath().getString("author"), updatedAuthor);
        Assert.assertEquals(response.jsonPath().getString("title"), updatedTitle);
        Assert.assertEquals(response.jsonPath().getString("isbn"), updatedIsbn);
        Assert.assertEquals(response.jsonPath().getString("releaseDate"), updatedReleaseDate);
    }

    @Test(priority = 5, description = "Check that the book is partially updated correctly")
    @Severity(SeverityLevel.CRITICAL)
    public void retrievePartiallyUpdatedBookTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .when()
                .get(endpoint + bookId)
                .then()
                .log().all()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(bookSchema))
                .extract().response();
        //Assertions
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getTime() < 3000);
        Assert.assertEquals(response.jsonPath().getString("author"), partialUpdatedAuthor);
        Assert.assertEquals(response.jsonPath().getString("title"), partialUpdatedTitle);
    }

    @Test(priority = 6, description = "Check that the book is deleted correctly")
    @Severity(SeverityLevel.CRITICAL)
    public void retrieveDeletedBookTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .when()
                .get(endpoint + bookId)
                .then()
                .log().all()
                .extract().response();
        //Assertions
        Assert.assertEquals(response.getStatusCode(), 404);
        Assert.assertTrue(response.getTime() < 3000);
        Assert.assertEquals(response.jsonPath().getString("message"), "Book not found");
    }
}
