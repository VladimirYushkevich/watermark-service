package com.company.watermark;

import com.company.watermark.domain.Book;
import com.company.watermark.domain.Journal;
import com.company.watermark.domain.Watermark;

import static com.company.watermark.domain.Book.Topic.SCIENCE;
import static com.company.watermark.domain.Watermark.Status.NEW;

/**
 * Simple factory to createOrUpdate data for testing.
 */
public class RepositoryDataFactory {

    public static Book createBook() {
        final Book book = Book.builder()
                .title("titleBook")
                .author("authorBook")
                .topic(SCIENCE)
                .build();

        book.setWatermark(Watermark.builder()
                .publication(book)
                .status(NEW.getName())
                .build());

        return book;
    }

    public static Journal createJournal() {
        final Journal journal = Journal.builder()
                .title("titleJournal")
                .author("authorJournal")
                .build();

        journal.setWatermark(Watermark.builder()
                .publication(journal)
                .status(NEW.getName())
                .build());

        return journal;
    }
}
