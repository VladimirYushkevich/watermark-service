package com.company.watermark.controller;

import com.company.watermark.dto.PublicationDTO;
import com.company.watermark.dto.PublicationRequestDTO;
import com.jayway.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import static com.company.watermark.domain.Content.JOURNAL;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

public class PublicationJournalControllerIT extends BaseControllerIT {

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
            .post(publicationBase + "/create").prettyPeek()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("errors.field", containsInAnyOrder("content", "title", "author"));

        RestAssured.given()
            .contentType(JSON)
            .body(objectMapper.writeValueAsString(PublicationDTO.builder().content(JOURNAL).build()))
        .when()
            .post(publicationBase + "/create").prettyPeek()
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
            .get(publicationBase + "/list").prettyPeek()
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("entries.findAll { it.id > 0 }.size()", is(2))
            .body("entries.content", hasItems("JOURNAL"))
            .body("entries.findAll { it.title != null }.size()", is(2))
            .body("entries.findAll { it.author != null }.size()", is(2));
    }

    @Test
    public void testFindJournal_success() throws Exception {
        final Long id = createJournal(journal);

        RestAssured.given()
            .parameter("content", "JOURNAL")
        .when()
            .get(publicationBase + "/{publication_id}", id).prettyPeek()
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", notNullValue())
            .body("content", is("JOURNAL"));
    }

    @Test
    public void testFindJournal_fail() throws Exception {
        final Long id = createJournal(journal);

        RestAssured.given()
            .parameter("content", "JOURNAL")
        .when()
            .get(publicationBase + "/{publication_id}", id * 10000).prettyPeek()
        .then()
            .statusCode(HttpStatus.NOT_FOUND.value());

        RestAssured.given()
            .parameter("content", "")
        .when()
            .get(publicationBase + "/{publication_id}", id).prettyPeek()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void testUpdateJournal_success() throws Exception {
        final Long id = createJournal(journal);

        RestAssured.given()
            .contentType(JSON)
            .body(objectMapper.writeValueAsString(PublicationDTO.builder()
                .id(id)
                .content(JOURNAL)
                .author("newAuthor")
                .title("newTitle")
                .build()))
        .when()
            .put(publicationBase + "/update").prettyPeek()
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", notNullValue())
            .body("content", is("JOURNAL"))
            .body("title", is("newTitle"))
            .body("author", is("newAuthor"));
    }

    @Test
    public void testDeleteJournal_success() throws Exception {
        final Long id = createJournal(journal);

        RestAssured.given()
            .contentType(JSON)
            .body(objectMapper.writeValueAsString(PublicationRequestDTO.builder()
                .publicationId(id)
                .content(JOURNAL)
                .build()))
        .when()
            .delete(publicationBase).prettyPeek()
        .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void testDeleteJournal_fail() throws Exception {
        final Long id = createJournal(journal);

        RestAssured.given()
            .contentType(JSON)
            .body(objectMapper.writeValueAsString(PublicationRequestDTO.builder()
               .publicationId(id * 10000)
               .content(null)
               .build()))
        .when()
            .delete(publicationBase).prettyPeek()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value());

        RestAssured.given()
            .contentType(JSON)
            .body(objectMapper.writeValueAsString(PublicationRequestDTO.builder()
                .publicationId(null)
                .content(JOURNAL)
                .build()))
        .when()
            .delete(publicationBase).prettyPeek()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value());

        RestAssured.given()
            .contentType(JSON)
            .body(objectMapper.writeValueAsString(PublicationRequestDTO.builder()
                 .publicationId(id * 10000)
                 .content(JOURNAL)
                 .build()))
        .when()
            .delete(publicationBase).prettyPeek()
        .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
