package com.company.watermark.controller;

import com.company.watermark.dto.PublicationDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import org.hamcrest.Matcher;
import org.junit.Before;
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
 * Base class for Integration Tests.
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

    protected PublicationDTO book;
    protected PublicationDTO journal;

    @Before
    public void setUp() throws Exception {
        book = objectMapper.readValue(getClass().getResourceAsStream("/json/publication_dto_book.json"), PublicationDTO.class);
        journal = objectMapper.readValue(getClass().getResourceAsStream("/json/publication_dto_journal.json"), PublicationDTO.class);
    }

    protected Long createAndVerifyPublication(PublicationDTO publication, Matcher<Object> topicMatcher) throws Exception {
        return RestAssured.given()
            .contentType(JSON)
            .body(objectMapper.writeValueAsString(publication))
        .when()
            .post(publicationBase + "/create").prettyPeek()
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", notNullValue())
            .body("content", is(publication.getContent().toString()))
            .body("title", is(publication.getTitle()))
            .body("author", is(publication.getAuthor()))
            .body("topic", topicMatcher)
        .extract()
            .jsonPath()
            .getLong("id");
    }
}
