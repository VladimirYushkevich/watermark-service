package com.company.watermark.repository;

import com.company.watermark.domain.Book;
import com.company.watermark.domain.Journal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.company.watermark.repository.RepositoryDataFactory.createBook;
import static com.company.watermark.repository.RepositoryDataFactory.createJournal;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PublicationRepositoryIntegrationTest {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private JournalRepository journalRepository;

    @Test
    public void testBookCrudOperations() {
        final Book book = bookRepository.save(createBook());

        assertThat(bookRepository.findAll().size(), is(1));
        assertThat(bookRepository.findOne(book.getId()), is(book));

        book.setAuthor("newAuthor");
        assertThat(bookRepository.findOne(book.getId()).getAuthor(), is("newAuthor"));

        bookRepository.delete(book.getId());
        assertThat(bookRepository.findAll().size(), is(0));
    }

    @Test
    public void testJournalCrudOperations() {
        final Journal journal = journalRepository.save(createJournal());

        assertThat(journalRepository.findAll().size(), is(1));
        assertThat(journalRepository.findOne(journal.getId()), is(journal));

        journal.setAuthor("newAuthor");
        assertThat(journalRepository.findOne(journal.getId()).getAuthor(), is("newAuthor"));

        journalRepository.delete(journal.getId());
        assertThat(journalRepository.findAll().size(), is(0));
    }
}
