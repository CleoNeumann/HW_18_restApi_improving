package tests;

import models.AddBookModel;
import models.ResponseModel;
import models.UserLoginModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static specs.BookStoreSpecs.*;

@Tag("smoke")
@DisplayName("Delete books tests")
public class BookStoreTests extends TestBase {

    @Test
    public void deleteBooks() {
        UserLoginModel loginData = new UserLoginModel();
        loginData.setUserName("admin");
        loginData.setPassword("Admin123!@#");
        ResponseModel authResponse = step("Make login request", () ->
                given(requestSpec)
                .body(loginData)
                .when()
                .post("/Account/v1/Login")
                .then()
                .spec(responseSpec200)
                .extract()
                .as(ResponseModel.class));
        step("Add cookies", () -> {
            open("/favicon.ico");
            getWebDriver().manage().addCookie(new Cookie("userID", authResponse.getUserId()));
            getWebDriver().manage().addCookie(new Cookie("expires", authResponse.getExpires()));
            getWebDriver().manage().addCookie(new Cookie("token", authResponse.getToken()));
        });
        step("Open profile", () -> {
            open("/profile");
        });
        step("Check success login/right user", () ->
            $("#userName-value").shouldHave(text(loginData.getUserName())));
        step("Check cart is empty", () ->
            $(".ReactTable").shouldHave(text("No rows found")));

        AddBookModel addBookData = new AddBookModel(authResponse.getUserId(), new String[]{"9781449325862"});
        step("Make request to add book", () ->
            given(requestSpec)
                .header("Authorization", "Bearer " + authResponse.getToken())
                .body(addBookData)
                .when()
                .post("/BookStore/v1/Books")
                .then()
                .spec(responseSpec201));
        step("Open profile", () ->
                open("/profile"));
        step("Check book success added", () ->
                $(".ReactTable").shouldHave(text("Git Pocket Guide")));

        step("Make delete request", () ->
            given(requestSpec)
                .queryParam("UserId", authResponse.getUserId())
                .header("Authorization", "Bearer " + authResponse.getToken())
                .when()
                .delete("/BookStore/v1/Books")
                .then()
                .spec(responseSpec204));
        step("Open profile", () ->
                open("/profile"));
        step("Check cart is empty again", () ->
                $(".ReactTable").shouldHave(text("No rows found")));
    }
}
