package com.company.watermark.controller;

import com.company.watermark.client.WatermarkClient;
import com.company.watermark.domain.Content;
import com.company.watermark.domain.Watermark;
import com.company.watermark.dto.PublicationDTO;
import com.company.watermark.dto.PublicationRequestDTO;
import com.jayway.restassured.RestAssured;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static com.company.watermark.domain.Watermark.Status.*;
import static com.jayway.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.*;

public abstract class BaseWatermarkControllerIT extends BaseControllerIT {

    @MockBean
    private WatermarkClient watermarkClient;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        when(watermarkClient.createWatermark(anyList())).thenCallRealMethod();
    }

    protected void testWatermarkPublicationAsync_success(PublicationDTO publicationDTO, Content content,
                                                         Matcher<Object> topicMatcher) throws Exception {
        Long publicationId1 = createAndVerifyPublication(publicationDTO, topicMatcher);
        Long publicationId2 = createAndVerifyPublication(publicationDTO, topicMatcher);

        delayWatermarkClient(2000L);

        getAndVerifyTicketId(publicationId1, content);
        getAndVerifyTicketId(publicationId2, content);

        //allow main thread to write everything in log
        Thread.sleep(5000L);
    }

    protected void testWatermarkTicketStatus_success(PublicationDTO publicationDTO, Content content,
                                                     Matcher<Object> topicMatcher) throws Exception {
        Long publicationId = createAndVerifyPublication(publicationDTO, topicMatcher);

        delayWatermarkClient(2000L);

        final UUID ticketId = getAndVerifyTicketId(publicationId, content);

        pollAndVerifyTicketStatus(ticketId, PENDING, nullValue());
    }

    protected void testWatermarkTicketStatusFlow_success(PublicationDTO publicationDTO, Content content,
                                                         Matcher<Object> topicMatcher) throws Exception {
        Long publicationId = createAndVerifyPublication(publicationDTO, topicMatcher);

        delayWatermarkClient(500L);

        final UUID ticketId = getAndVerifyTicketId(publicationId, content);

        pollAndVerifyTicketStatus(ticketId, PENDING, nullValue());

        Thread.sleep(1000L);

        pollAndVerifyTicketStatus(ticketId, SUCCESS, notNullValue());
    }

    protected void testWatermarkTicketStatusFlow_fail_timeout(PublicationDTO publicationDTO, Content content,
                                                              Matcher<Object> topicMatcher) throws Exception {
        Long publicationId = createAndVerifyPublication(publicationDTO, topicMatcher);

        delayWatermarkClient(3100L);

        final UUID ticketId = getAndVerifyTicketId(publicationId, content);

        pollAndVerifyTicketStatus(ticketId, PENDING, nullValue());

        Thread.sleep(3100L);

        pollAndVerifyTicketStatus(ticketId, FAILED, nullValue());
    }

    private void pollAndVerifyTicketStatus(UUID ticketId, Watermark.Status status, Matcher<Object> documentMatcher) throws Exception {
        RestAssured.when()
            .get(watermarkBase + "/{ticket_id}", ticketId).prettyPeek()
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", is(ticketId.toString()))
            .body("status", is(status.toString()))
            .body("document", documentMatcher);
    }

    private UUID getAndVerifyTicketId(Long publicationId, Content content) throws Exception {
        return RestAssured.given()
            .contentType(JSON)
            .body(objectMapper.writeValueAsString(PublicationRequestDTO.builder()
                .publicationId(publicationId)
                .content(content)
                .build()))
        .when()
            .post(watermarkBase).prettyPeek()
        .then()
            .statusCode(HttpStatus.OK.value())
            .body(not(""))
        .extract()
            .as(UUID.class);
    }

    private void delayWatermarkClient(long timeout) {
        doAnswer(invocation -> {
            Thread.sleep(timeout);
            return "watermark";
        }).when(watermarkClient).createWatermark(any());
    }

    @After
    public void tearDown() throws Exception {
        reset(watermarkClient);
    }
}
