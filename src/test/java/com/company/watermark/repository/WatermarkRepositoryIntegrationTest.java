package com.company.watermark.repository;

import com.company.watermark.domain.Book;
import com.company.watermark.domain.Journal;
import com.company.watermark.domain.Watermark;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static com.company.watermark.domain.Watermark.Status.FINISHED;
import static com.company.watermark.domain.Watermark.Status.PENDING;
import static com.company.watermark.repository.RepositoryDataFactory.createBook;
import static com.company.watermark.repository.RepositoryDataFactory.createJournal;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class WatermarkRepositoryIntegrationTest {

    @Autowired
    private WatermarkRepository watermarkRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private JournalRepository journalRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testWatermarkForBookCrudOperations() throws Exception {
        final Book book = createBook();
        entityManager.persist(book);

        assertThat(bookRepository.findOne(book.getId()), is(book));
        assertNull(bookRepository.findOne(book.getId()).getWatermark());

        //create watermark
        final Watermark watermark = Watermark.builder()
                .publication(book)
                .status(PENDING.getName())
                .build();
        book.setWatermark(watermark);
        entityManager.persist(watermark);
        assertThat(bookRepository.findOne(book.getId()).getWatermark().getStatus(), is(PENDING));

        final UUID watermarkId = watermark.getId();
        assertThat(watermarkRepository.findById(watermarkId), is(watermark));
        assertThat(watermarkRepository.findById(watermarkId).getPublication(), is(book));

        //update watermark
        watermark.setStatus(FINISHED.getName());
        assertThat(bookRepository.findOne(book.getId()).getWatermark().getStatus(), is(FINISHED));

        //delete watermark
        book.getWatermark().setPublication(null);
        book.setWatermark(null);
        assertNull(bookRepository.findOne(book.getId()).getWatermark());
        assertTrue(watermarkRepository.findAll().isEmpty());
    }

    @Test
    public void testWatermarkForJournalCrudOperations() throws Exception {
        final Journal journal = createJournal();
        entityManager.persist(journal);

        assertThat(journalRepository.findOne(journal.getId()), is(journal));
        assertNull(journalRepository.findOne(journal.getId()).getWatermark());

        //create watermark
        final Watermark watermark = Watermark.builder()
                .publication(journal)
                .status(PENDING.getName())
                .build();
        journal.setWatermark(watermark);
        entityManager.persist(watermark);
        assertThat(journalRepository.findOne(journal.getId()).getWatermark().getStatus(), is(PENDING));

        final UUID watermarkId = watermark.getId();
        assertThat(watermarkRepository.findById(watermarkId), is(watermark));
        assertThat(watermarkRepository.findById(watermarkId).getPublication(), is(journal));

        //update watermark
        watermark.setStatus(FINISHED.getName());
        assertThat(journalRepository.findOne(journal.getId()).getWatermark().getStatus(), is(FINISHED));

        //delete watermark
        journal.getWatermark().setPublication(null);
        journal.setWatermark(null);
        assertNull(journalRepository.findOne(journal.getId()).getWatermark());
        assertTrue(watermarkRepository.findAll().isEmpty());
    }
}
