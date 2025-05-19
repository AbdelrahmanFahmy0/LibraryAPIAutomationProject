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

import java.util.Map;

import static Utils.AllureUtils.allureFilter;

@Epic("Books")
@Feature("Retrieve All Book")
public class TC03_RetrieveAllBooks extends TestBase {

    //Variables
    String endpoint = "/books";
    String author = bookData.getJsonData("books[0].author");
    String title = bookData.getJsonData("books[0].title");
    String isbn = bookData.getJsonData("books[0].isbn");
    String releaseDate = bookData.getJsonData("books[0].releaseDate");

    //Methods
    @Test(priority = 1, description = "Check that the added book is present in the list of all books")
    @Severity(SeverityLevel.BLOCKER)
    public void retrieveAllBooks() {
        Response response = RestAssured.given()
                .filter(allureFilter())
                .header("Content-Type", "application/json")
                .header("g-token", token)
                .when()
                .get(endpoint)
                .then()
                .log().all()
                .extract().response();

        //Find the book with the specified id
        Map<String, Object> book = response.jsonPath().getMap("find { it.id == " + bookId + " }");

        //Assertions
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getTime() < 3000);
        Assert.assertEquals(book.get("author"), author);
        Assert.assertEquals(book.get("title"), title);
        Assert.assertEquals(book.get("isbn"), isbn);
        Assert.assertEquals(book.get("releaseDate"), releaseDate);
    }

    @Test(priority = 2, description = "Check that user can't retrieve all books with invalid token")
    @Severity(SeverityLevel.CRITICAL)
    public void retrieveAllBooksWithInvalidToken() {
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

    @Test(priority = 2, description = "Check that user can't retrieve all books without token")
    @Severity(SeverityLevel.CRITICAL)
    public void retrieveAllBooksWithoutToken() {
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
