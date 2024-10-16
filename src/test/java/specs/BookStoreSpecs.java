package specs;

import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.http.ContentType.JSON;

public class BookStoreSpecs {
    public static RequestSpecification requestSpec = RestAssured.with()
            .filter(withCustomTemplates())
            .contentType(JSON)
            .log().all();

    private static ResponseSpecification responseSpec(int statusCode) {
        return new ResponseSpecBuilder()
                .expectStatusCode(statusCode)
                .log(ALL)
                .build();
    }

    public static ResponseSpecification responseSpec200 = responseSpec(200);
    public static ResponseSpecification responseSpec201 = responseSpec(201);
    public static ResponseSpecification responseSpec204 = responseSpec(204);
}
