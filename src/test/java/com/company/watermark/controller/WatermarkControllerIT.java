package com.company.watermark.controller;

import org.junit.Test;

import static com.company.watermark.domain.Content.BOOK;
import static com.company.watermark.domain.Content.JOURNAL;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public class WatermarkControllerIT extends BaseWatermarkControllerIT {

    @Test
    public void testWatermarkPublicationAsync_success() throws Exception {
        testWatermarkPublicationAsync_success(book, BOOK, notNullValue());
        testWatermarkPublicationAsync_success(journal, JOURNAL, nullValue());
    }

    @Test
    public void testWatermarkTicketStatus_success() throws Exception {
        testWatermarkTicketStatus_success(book, BOOK, notNullValue());
        testWatermarkTicketStatus_success(journal, JOURNAL, nullValue());
    }

    @Test
    public void testWatermarkTicketStatusFlow_success() throws Exception {
        testWatermarkTicketStatusFlow_success(book, BOOK, notNullValue());
        testWatermarkTicketStatusFlow_success(journal, JOURNAL, nullValue());
    }

    @Test
    public void testWatermarkTicketStatusFlow_fail_timeout() throws Exception {
        testWatermarkTicketStatusFlow_fail_timeout(book, BOOK, notNullValue());
        testWatermarkTicketStatusFlow_fail_timeout(journal, JOURNAL, nullValue());
    }
}
