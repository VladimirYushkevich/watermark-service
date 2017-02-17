package com.company.watermark.controller;

import com.company.watermark.dto.PublicationDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static com.company.watermark.domain.enums.Content.JOURNAL;
import static com.company.watermark.domain.enums.Topic.SCIENCE;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PublicationJournalControllerIT {

    @Autowired
    private ObjectMapper objectMapper;
    @Value("http://localhost:${local.server.port}/publication")
    private String base;

    private PublicationDTO journal;

    @Before
    public void setUp() throws Exception {
        journal = objectMapper.readValue(getClass().getResourceAsStream("/json/publication_dto_journal.json"), PublicationDTO.class);
    }

    @Test
    public void testCreateJournal_fail() throws Exception {
        RestAssured.given()
            .contentType(JSON)
            .body(objectMapper.writeValueAsString(new PublicationDTO()))
        .when()
            .post(base + "/create").prettyPeek()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("errors.field", containsInAnyOrder("content", "title", "author"));

        RestAssured.given()
            .contentType(JSON)
            .body(objectMapper.writeValueAsString(PublicationDTO.builder().content(JOURNAL).build()))
        .when()
            .post(base + "/create").prettyPeek()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("errors.field", containsInAnyOrder("title", "author"));
    }

    @Test
    public void testJournalList_success() throws Exception {
        RestAssured.given()
            .parameter("content", "JOURNAL")
            .and().parameter("page", 0)
            .and().parameter("size", 2)
        .when()
            .get(base + "/list").prettyPeek()
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("entries.findAll { it.id > 0 }.size()", is(2))
            .body("entries.content", hasItems("JOURNAL"))
            .body("entries.findAll { it.title != null }.size()", is(2))
            .body("entries.findAll { it.author != null }.size()", is(2));
    }

    @Test
    public void testFindJournal_success() throws Exception {
        final Long id = createJournal();

        RestAssured.given()
            .parameter("content", "JOURNAL")
        .when()
            .get(base + "/{publication_id}", id).prettyPeek()
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", notNullValue())
            .body("content", is("JOURNAL"));
    }

    @Test
    public void testFindJournal_fail() throws Exception {
        final Long id = createJournal();

        RestAssured.given()
            .parameter("content", "JOURNAL")
        .when()
            .get(base + "/{publication_id}", id * 10000).prettyPeek()
        .then()
            .statusCode(HttpStatus.NOT_FOUND.value());

        RestAssured.given()
            .parameter("content", "")
        .when()
            .get(base + "/{publication_id}", id).prettyPeek()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void testUpdateJournal_success() throws Exception {
        final Long id = createJournal();

        RestAssured.given()
            .contentType(JSON)
            .body(objectMapper.writeValueAsString(PublicationDTO.builder()
            .id(id)
            .content(JOURNAL)
            .author("newAuthor")
            .title("newTitle")
            .topic(SCIENCE)
            .build()))
        .when()
            .put(base + "/update").prettyPeek()
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", notNullValue())
            .body("content", is("JOURNAL"))
            .body("title", is("newTitle"))
            .body("author", is("newAuthor"));
    }

    @Test
    public void testDeleteJournal_success() throws Exception {
        final Long id = createJournal();

        RestAssured.given()
            .parameter("content", "JOURNAL")
        .when()
            .delete(base + "/{publication_id}", id).prettyPeek()
        .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void testDeleteJournal_fail() throws Exception {
        final Long id = createJournal();

        RestAssured.given()
            .parameter("content", "JOURNAL")
        .when()
            .delete(base + "/{publication_id}", id * 10000).prettyPeek()
        .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }

    private Long createJournal() throws Exception {
        return RestAssured.given()
            .contentType(JSON)
            .body(objectMapper.writeValueAsString(journal))
        .when()
            .post(base + "/create").prettyPeek()
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
