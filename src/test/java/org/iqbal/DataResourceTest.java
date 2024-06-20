package org.iqbal;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class DataResourceTest {

    @Test
    public void testUploadEndpoint() {
        File jsonFile = new File("src/test/resources/test-data.json");

        given()
                .multiPart("file", jsonFile, MediaType.APPLICATION_JSON)
                .when()
                .post("/data/upload")
                .then()
                .statusCode(200)
                .body(is("File uploaded and processed successfully"));
    }
}
