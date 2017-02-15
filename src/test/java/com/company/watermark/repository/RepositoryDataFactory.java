package com.company.watermark.repository;

import com.company.watermark.domain.Book;
import com.company.watermark.domain.Journal;

import static com.company.watermark.domain.Book.Topic.SCIENCE;

/**
 * Simple factory to create data for testing.
 */
public class RepositoryDataFactory {

    public static Book createBook() {
        return Book.builder()
                .title("titleBook")
                .author("authorBook")
                .topic(SCIENCE)
                .build();
    }

    public static Journal createJournal() {
        return Journal.builder()
                .title("titleJournal")
                .author("authorJournal")
                .build();
    }
}
