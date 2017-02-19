package com.company.watermark.service.impl;

import com.company.watermark.exception.WatermarkException;
import org.junit.Test;

import static com.company.watermark.domain.Content.BOOK;
import static com.company.watermark.domain.Content.JOURNAL;
import static org.junit.Assert.fail;

public class PublicationServiceIT extends BasePublicationServiceIT {

    @Test
    public void testPublicationCrudOperations() throws Exception {
        testPublicationCrudOperations(BOOK, "---*book*authorBook*titleBook*Science*---");
        testPublicationCrudOperations(JOURNAL, "---*journal*authorJournal*titleJournal*---");
    }

    @Test
    public void testWatermarkSetUp() throws Exception {
        try {
            testWatermarkSetUp(BOOK);
            fail("Should throw exception");
        } catch (WatermarkException ignored) {
        }
        try {
            testWatermarkSetUp(JOURNAL);
            fail("Should throw exception");
        } catch (WatermarkException ignored) {
        }
    }

    @Test
    public void testWatermarkStatusUpdateForPublication() throws Exception {
        testWatermarkStatusUpdateForPublication(BOOK);
        testWatermarkStatusUpdateForPublication(JOURNAL);
    }
}
