package com.company.watermark.controller;

import com.google.common.collect.Lists;
import org.junit.Test;

import static com.company.watermark.domain.Content.BOOK;
import static com.company.watermark.domain.Content.JOURNAL;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public class PublicationControllerIT extends BasePublicationControllerIT {

    @Test
    public void testCreatePublication_fail() throws Exception {
        testCreatePublication_fail(BOOK, Lists.newArrayList("title", "author", "topic"));
        testCreatePublication_fail(JOURNAL, Lists.newArrayList("title", "author"));
    }

    @Test
    public void testPublicationList_success() throws Exception {
        testPublicationList_success(BOOK, 2);
        testPublicationList_success(JOURNAL, 0);
    }

    @Test
    public void testFindPublication_success() throws Exception {
        testFindPublication_success(book, notNullValue());
        testFindPublication_success(journal, nullValue());
    }

    @Test
    public void testFindPublication_fail() throws Exception {
        testFindPublication_fail(book, notNullValue());
        testFindPublication_fail(journal, nullValue());
    }

    @Test
    public void testDeletePublication_success() throws Exception {
        testDeletePublication_success(book, notNullValue());
        testDeletePublication_success(journal, nullValue());
    }

    @Test
    public void testDeletePublication_fail() throws Exception {
        testDeletePublication_fail(book, notNullValue());
        testDeletePublication_fail(journal, nullValue());
    }
}
