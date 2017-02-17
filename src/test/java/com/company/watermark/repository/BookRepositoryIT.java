package com.company.watermark.repository;

import com.company.watermark.domain.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.company.watermark.RepositoryDataFactory.createBook;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BookRepositoryIT {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testBookCrudOperations() {
        //given
        final Book book = createBook();
        //when
        entityManager.persist(book);
        //then
        assertThat(bookRepository.findAll().size(), is(1));
        assertThat(bookRepository.findOne(book.getId()), is(book));

        //given
        book.setAuthor("newAuthor");
        //when
        entityManager.persist(book);
        //then
        assertThat(bookRepository.findOne(book.getId()).getAuthor(), is("newAuthor"));

        //when
        bookRepository.delete(book.getId());
        //then
        assertThat(bookRepository.findAll().size(), is(0));
    }

    @Test
    public void testBookPageable() throws Exception {
        //given
        entityManager.persist(createBook());
        entityManager.persist(createBook());
        entityManager.persist(createBook());

        //when
        final Page<Book> bookPage1 = bookRepository.findAll(new PageRequest(0, 2));
        //then
        assertThat(bookPage1.getTotalElements(), is(3L));
        assertThat(bookPage1.getContent().size(), is(2));

        //when
        final Page<Book> bookPage2 = bookRepository.findAll(new PageRequest(1, 2));
        //then
        assertThat(bookPage2.getTotalElements(), is(3L));
        assertThat(bookPage2.getContent().size(), is(1));
    }
}
