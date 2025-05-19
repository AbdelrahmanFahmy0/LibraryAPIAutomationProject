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

import static Models.Books.UpdateBookBody.getUpdateBookBody;
import static Utils.AllureUtils.allureFilter;

@Epic("Books")
@Feature("Update Book")
public class TC04_UpdateBook extends TestBase {

    //Variables
    String endpoint = "/books/";
    String author = bookData.getJsonData("books[1].author");
    String title = bookData.getJsonData("books[1].title");
    String isbn = bookData.getJsonData("books[1].isbn");
    String releaseDate = bookData.getJsonData("books[1].releaseDate");

    //Methods
    @Test(priority = 1, description = "Check that user can update a book")
    @Severity(SeverityLevel.BLOCKER)
    public void updateBookTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Authorization", "Basic " + credentials)
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .body(getUpdateBookBody(title, author, isbn, releaseDate))
                .when()
                .put(endpoint + bookId)
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

    @Test(priority = 2, description = "Check that user cannot update a book with invalid credentials")
    @Severity(SeverityLevel.CRITICAL)
    public void updateBookWithInvalidCredentialsTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Authorization", "Basic hjkdsfkjdskfj")
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .body(getUpdateBookBody(title, author, isbn, releaseDate))
                .when()
                .put(endpoint + bookId)
                .then()
                .log().all()
                .extract().response();
        //Assertions
        Assert.assertEquals(response.getStatusCode(), 401);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 2, description = "Check that user cannot update a book without credentials")
    @Severity(SeverityLevel.CRITICAL)
    public void updateBookWithoutCredentialsTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .body(getUpdateBookBody(title, author, isbn, releaseDate))
                .when()
                .put(endpoint + bookId)
                .then()
                .log().all()
                .extract().response();
        //Assertions
        Assert.assertEquals(response.getStatusCode(), 401);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 2, description = "Check that user cannot update a book with invalid token")
    @Severity(SeverityLevel.CRITICAL)
    public void updateBookWithInvalidTokenTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Authorization", "Basic " + credentials)
                .header("Content-Type", "application/json")
                .header("g-token", invalidToken)
                .body(getUpdateBookBody(title, author, isbn, releaseDate))
                .when()
                .put(endpoint + bookId)
                .then()
                .log().all()
                .extract().response();
        //Assertions
        Assert.assertEquals(response.getStatusCode(), 403);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 2, description = "Check that user cannot update a book without token")
    @Severity(SeverityLevel.CRITICAL)
    public void updateBookWithoutTokenTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Authorization", "Basic " + credentials)
                .header("Content-Type", "application/json")
                .body(getUpdateBookBody(title, author, isbn, releaseDate))
                .when()
                .put(endpoint + bookId)
                .then()
                .log().all()
                .extract().response();
        //Assertions
        Assert.assertEquals(response.getStatusCode(), 403);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 3, description = "Check that user cannot update a book with invalid ID")
    public void updateBookWithInvalidIDTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Authorization", "Basic " + credentials)
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .body(getUpdateBookBody(title, author, isbn, releaseDate))
                .when()
                .put(endpoint + invalidBookId)
                .then()
                .log().all()
                .extract().response();
        //Assertions
        Assert.assertEquals(response.getStatusCode(), 404);
        Assert.assertTrue(response.getTime() < 3000);
        Assert.assertEquals(response.jsonPath().getString("error"), "Book not found");
    }
}
