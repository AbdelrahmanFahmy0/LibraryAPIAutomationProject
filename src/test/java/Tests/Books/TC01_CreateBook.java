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

import static Models.Books.CreateBookBody.getCreateBookBody;
import static Utils.AllureUtils.allureFilter;

@Epic("Books")
@Feature("Create Book")
public class TC01_CreateBook extends TestBase {

    //Variables
    String endpoint = "/books";
    String author = bookData.getJsonData("books[0].author");
    String title = bookData.getJsonData("books[0].title");
    String isbn = bookData.getJsonData("books[0].isbn");
    String releaseDate = bookData.getJsonData("books[0].releaseDate");

    //Methods
    @Test(priority = 1, description = "Check that user can create a new book")
    @Severity(SeverityLevel.BLOCKER)
    public void createBookTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .body(getCreateBookBody(title, author, isbn, releaseDate))
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(bookSchema))
                .extract().response();
        //Assertions
        Assert.assertEquals(response.getStatusCode(), 201);
        Assert.assertTrue(response.getTime() < 3000);
        Assert.assertEquals(response.jsonPath().getString("author"), author);
        Assert.assertEquals(response.jsonPath().getString("title"), title);
        Assert.assertEquals(response.jsonPath().getString("isbn"), isbn);
        Assert.assertEquals(response.jsonPath().getString("releaseDate"), releaseDate);
        //Set Book ID
        bookId = response.jsonPath().getInt("id");
    }

    @Test(priority = 2, description = "Check that user can't create a new book without token")
    @Severity(SeverityLevel.CRITICAL)
    public void createBookWithoutTokenTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .body(getCreateBookBody(title, author, isbn, releaseDate))
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .extract().response();
        //Assertions
        Assert.assertEquals(response.getStatusCode(), 403);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 2, description = "Check that user can't create a new book with invalid token")
    @Severity(SeverityLevel.CRITICAL)
    public void createBookWithInvalidTokenTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", invalidToken)
                .body(getCreateBookBody(title, author, isbn, releaseDate))
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .extract().response();
        //Assertions
        Assert.assertEquals(response.getStatusCode(), 403);
        Assert.assertTrue(response.getTime() < 3000);
    }

    @Test(priority = 3, description = "Check that user can't create a new book without body")
    public void createBookWithoutBodyTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .extract().response();
        //Assertions
        Assert.assertEquals(response.getStatusCode(), 500);
        Assert.assertTrue(response.getTime() < 3000);
        Assert.assertEquals(response.jsonPath().getString("error"), "Title cannot be null");
    }

    @Test(priority = 3, description = "Check that user can't create a new book without title")
    public void createBookWithoutTitleTC() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .body(getCreateBookBody("", author, isbn, releaseDate))
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .extract().response();
        //Assertions
        Assert.assertEquals(response.getStatusCode(), 500);
        Assert.assertTrue(response.getTime() < 3000);
        Assert.assertEquals(response.jsonPath().getString("error"), "Title cannot be null");
    }
}
