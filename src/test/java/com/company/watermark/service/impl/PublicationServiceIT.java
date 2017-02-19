package com.company.watermark.service.impl;

import com.company.watermark.client.WatermarkClient;
import com.company.watermark.domain.Book;
import com.company.watermark.domain.Journal;
import com.company.watermark.domain.Publication;
import com.company.watermark.domain.Watermark;
import com.company.watermark.exception.NotFoundException;
import com.company.watermark.exception.WatermarkException;
import com.company.watermark.service.PublicationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.company.watermark.RepositoryDataFactory.createBook;
import static com.company.watermark.RepositoryDataFactory.createJournal;
import static com.company.watermark.domain.Content.BOOK;
import static com.company.watermark.domain.Content.JOURNAL;
import static com.company.watermark.utils.WatermarkGenerator.generateWatermark;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ComponentScan("com.company.watermark.service.impl")
@DataJpaTest
public class PublicationServiceIT {

    @Autowired
    private PublicationService publicationService;
    @MockBean
    private WatermarkClient watermarkClient;

    @Test
    public void testBookCrudOperations() throws Exception {
        //create
        //when
        final Publication book = publicationService.createOrUpdate(createBook());
        //then
        final Long id = book.getId();
        assertNotNull(id);

        //when
        final Watermark watermark = publicationService.find(id, BOOK).getWatermark();
        //then
        assertThat(watermark.getPublication(), is(book));

        //update
        //given
        watermark.setProperty(generateWatermark(watermark.getPublication().getWatermarkProperties()));
        //when
        final String property = publicationService.find(id, BOOK).getWatermark().getProperty();
        //then
        assertThat(property, is("---*book*authorBook*titleBook*Science*---"));

        //delete
        //when
        publicationService.delete(id, BOOK);
        //then
        try {
            publicationService.find(id, BOOK);
            fail("Should throw exception");
        } catch (NotFoundException ignored) {
        }

        //pageable
        //given
        publicationService.createOrUpdate(createBook());
        publicationService.createOrUpdate(createBook());
        publicationService.createOrUpdate(createBook());

        //when
        final Page<Publication> bookPage1 = publicationService.findAllByPage(new PageRequest(0, 2), BOOK);
        //then
        assertThat(bookPage1.getTotalElements(), is(3L));
        assertThat(bookPage1.getContent().size(), is(2));

        //when
        final Page<Publication> bookPage2 = publicationService.findAllByPage(new PageRequest(1, 2), BOOK);
        //then
        assertThat(bookPage2.getTotalElements(), is(3L));
        assertThat(bookPage2.getContent().size(), is(1));
    }

    @Test
    public void testJournalCrudOperations() throws Exception {
        //create
        //when
        final Publication journal = publicationService.createOrUpdate(createJournal());
        //then
        final Long id = journal.getId();
        assertNotNull(id);

        //when
        final Watermark watermark = publicationService.find(id, JOURNAL).getWatermark();
        //then
        assertThat(watermark.getPublication(), is(journal));

        //update
        //given
        watermark.setProperty(generateWatermark(watermark.getPublication().getWatermarkProperties()));
        //when
        final String property = publicationService.find(id, JOURNAL).getWatermark().getProperty();
        //then
        assertThat(property, is("---*journal*authorJournal*titleJournal*---"));

        //delete
        //when
        publicationService.delete(id, JOURNAL);
        //then
        try {
            publicationService.find(id, JOURNAL);
            fail("Should throw exception");
        } catch (NotFoundException ignored) {
        }

        //pageable
        //given
        publicationService.createOrUpdate(createJournal());
        publicationService.createOrUpdate(createJournal());
        publicationService.createOrUpdate(createJournal());

        //when
        final Page<Publication> journalPage1 = publicationService.findAllByPage(new PageRequest(0, 2), JOURNAL);
        //then
        assertThat(journalPage1.getTotalElements(), is(3L));
        assertThat(journalPage1.getContent().size(), is(2));

        //when
        final Page<Publication> journalPage2 = publicationService.findAllByPage(new PageRequest(1, 2), JOURNAL);
        //then
        assertThat(journalPage2.getTotalElements(), is(3L));
        assertThat(journalPage2.getContent().size(), is(1));
    }

    @Test
    public void testWatermarkSetUpForBook_success() throws Exception {
        //given
        final Book book = createBook();
        book.setWatermark(null);
        final Publication bookWithoutWatermark = publicationService.createOrUpdate(book);
        //when
        final Watermark watermark = publicationService.setWatermark(bookWithoutWatermark.getId(), BOOK);
        //then
        assertThat(publicationService.find(book.getId(), BOOK).getWatermark(), is(watermark));
    }

    @Test
    public void testWatermarkSetUpForJournal_success() throws Exception {
        //given
        final Journal journal = createJournal();
        journal.setWatermark(null);
        final Publication journalWithoutWatermark = publicationService.createOrUpdate(journal);
        //when
        final Watermark watermark = publicationService.setWatermark(journalWithoutWatermark.getId(), JOURNAL);
        //then
        assertThat(publicationService.find(journal.getId(), JOURNAL).getWatermark(), is(watermark));
    }

    @Test(expected = WatermarkException.class)
    public void testWatermarkSetUpForBook_fail() throws Exception {
        //given
        final Publication book = publicationService.createOrUpdate(createBook());
        //when
        publicationService.setWatermark(book.getId(), BOOK);
    }

    @Test(expected = WatermarkException.class)
    public void testWatermarkSetUpForJournal_fail() throws Exception {
        //given
        final Publication journal = publicationService.createOrUpdate(createJournal());
        //when
        publicationService.setWatermark(journal.getId(), JOURNAL);
    }
}
