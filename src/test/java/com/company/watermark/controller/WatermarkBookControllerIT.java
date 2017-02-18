package com.company.watermark.controller;

import com.company.watermark.dto.PublicationDTO;
import com.company.watermark.dto.PublicationRequestDTO;
import com.jayway.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static com.company.watermark.domain.Content.BOOK;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.not;

public class WatermarkBookControllerIT extends BaseControllerIT {

    private PublicationDTO book;
    private Long bookId1;
    private Long bookId2;

    @Before
    public void setUp() throws Exception {
        book = objectMapper.readValue(getClass().getResourceAsStream("/json/publication_dto_book.json"), PublicationDTO.class);

        bookId1 = createBook(book);
        bookId2 = createBook(book);
    }

    @Test
    public void testWatermarkBook_success() throws Exception {

        final UUID ticketId1 = getTicketId(bookId1);
        final UUID ticketId2 = getTicketId(bookId2);

        Thread.sleep(5000);
    }

    private UUID getTicketId(Long publicationId) throws Exception {

        return RestAssured.given()
            .contentType(JSON)
            .body(objectMapper.writeValueAsString(PublicationRequestDTO.builder()
                .publicationId(publicationId)
                .content(BOOK)
                .build()))
        .when()
            .post(watermarkBase).prettyPeek()
        .then()
            .statusCode(HttpStatus.OK.value())
            .body(not(""))
        .extract()
            .as(UUID.class);
    }
}
