package com.company.watermark.controller;

import com.company.watermark.dto.PublicationDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static com.jayway.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Base class for controller Integration Tests.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class BaseControllerIT {

    @Autowired
    protected ObjectMapper objectMapper;
    @Value("http://localhost:${local.server.port}/publication")
    protected String publicationBase;
    @Value("http://localhost:${local.server.port}/watermark")
    protected String watermarkBase;

    protected Long createBook(PublicationDTO book) throws Exception {
        return RestAssured.given()
            .contentType(JSON)
            .body(objectMapper.writeValueAsString(book))
        .when()
            .post(publicationBase + "/create").prettyPeek()
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", notNullValue())
            .body("content", is("BOOK"))
            .body("title", is("bookTitle"))
            .body("author", is("bookAuthor"))
            .body("topic", is("BUSINESS"))
        .extract()
            .jsonPath()
            .getLong("id");
    }

    protected Long createJournal(PublicationDTO journal) throws Exception {
        return RestAssured.given()
            .contentType(JSON)
            .body(objectMapper.writeValueAsString(journal))
        .when()
            .post(publicationBase + "/create").prettyPeek()
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", notNullValue())
            .body("content", is("JOURNAL"))
            .body("title", is("journalTitle"))
            .body("author", is("journalAuthor"))
        .extract()
            .jsonPath()
            .getLong("id");
    }
}
