package com.company.watermark.controller;

import com.company.watermark.dto.PublicationDTO;
import com.company.watermark.dto.PublicationRequestDTO;
import com.jayway.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import static com.company.watermark.domain.Book.Topic.SCIENCE;
import static com.company.watermark.domain.Content.BOOK;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

public class PublicationBookControllerIT extends BaseControllerIT {

    private PublicationDTO book;

    @Before
    public void setUp() throws Exception {
        book = objectMapper.readValue(getClass().getResourceAsStream("/json/publication_dto_book.json"), PublicationDTO.class);
    }

    @Test
    public void testCreateBook_fail() throws Exception {
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
            .body(objectMapper.writeValueAsString(PublicationDTO.builder().content(BOOK).build()))
        .when()
            .post(publicationBase + "/create").prettyPeek()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("errors.field", containsInAnyOrder("topic", "title", "author"));
    }

    @Test
    public void testBookList_success() throws Exception {
        RestAssured.given()
            .parameter("content", "BOOK")
            .and().parameter("page", 0)
            .and().parameter("size", 2)
        .when()
            .get(publicationBase + "/list").prettyPeek()
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("entries.findAll { it.id > 0 }.size()", is(2))
            .body("entries.content", hasItems("BOOK"))
            .body("entries.findAll { it.title != null }.size()", is(2))
            .body("entries.findAll { it.author != null }.size()", is(2))
            .body("entries.findAll { it.topic != null }.size()", is(2));
    }

    @Test
    public void testFindBook_success() throws Exception {
        final Long id = createBook(book);

        RestAssured.given()
            .parameter("content", "BOOK")
        .when()
            .get(publicationBase + "/{publication_id}", id).prettyPeek()
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", notNullValue())
            .body("content", is("BOOK"));
    }

    @Test
    public void testFindBook_fail() throws Exception {
        final Long id = createBook(book);

        RestAssured.given()
            .parameter("content", "BOOK")
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
    public void testUpdateBook_success() throws Exception {
        final Long id = createBook(book);

        RestAssured.given()
            .contentType(JSON)
            .body(objectMapper.writeValueAsString(PublicationDTO.builder()
                .id(id)
                .content(BOOK)
                .author("newAuthor")
                .title("newTitle")
                .topic(SCIENCE)
                .build()))
        .when()
            .put(publicationBase + "/update").prettyPeek()
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", notNullValue())
            .body("content", is("BOOK"))
            .body("title", is("newTitle"))
            .body("author", is("newAuthor"))
            .body("topic", is("SCIENCE"));
    }

    @Test
    public void testDeleteBook_success() throws Exception {
        final Long id = createBook(book);

        RestAssured.given()
            .contentType(JSON)
            .body(objectMapper.writeValueAsString(PublicationRequestDTO.builder()
                .publicationId(id)
                .content(BOOK)
                .build()))
        .when()
            .delete(publicationBase).prettyPeek()
        .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void testDeleteBook_fail() throws Exception {
        final Long id = createBook(book);

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
                .content(BOOK)
                .build()))
        .when()
            .delete(publicationBase).prettyPeek()
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value());

        RestAssured.given()
            .contentType(JSON)
            .body(objectMapper.writeValueAsString(PublicationRequestDTO.builder()
                 .publicationId(id * 10000)
                 .content(BOOK)
                 .build()))
        .when()
            .delete(publicationBase).prettyPeek()
        .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
