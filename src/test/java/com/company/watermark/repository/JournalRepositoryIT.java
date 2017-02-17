package com.company.watermark.repository;

import com.company.watermark.domain.Journal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.company.watermark.RepositoryDataFactory.createJournal;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class JournalRepositoryIT {

    @Autowired
    private JournalRepository journalRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testJournalCrudOperations() {
        //given
        final Journal journal = createJournal();
        //when
        entityManager.persist(journal);
        //then
        assertThat(journalRepository.findAll().size(), is(1));
        assertThat(journalRepository.findOne(journal.getId()), is(journal));

        //given
        journal.setAuthor("newAuthor");
        //when
        entityManager.persist(journal);
        //then
        assertThat(journalRepository.findOne(journal.getId()).getAuthor(), is("newAuthor"));


        //when
        journalRepository.delete(journal.getId());
        //then
        assertThat(journalRepository.findAll().size(), is(0));
    }

    @Test
    public void testJournalPageable() throws Exception {
        //given
        entityManager.persist(createJournal());
        entityManager.persist(createJournal());
        entityManager.persist(createJournal());

        //when
        final Page<Journal> journalPage1 = journalRepository.findAll(new PageRequest(0, 2));
        //then
        assertThat(journalPage1.getTotalElements(), is(3L));
        assertThat(journalPage1.getContent().size(), is(2));

        //when
        final Page<Journal> journalPage2 = journalRepository.findAll(new PageRequest(1, 2));
        //then
        assertThat(journalPage2.getTotalElements(), is(3L));
        assertThat(journalPage2.getContent().size(), is(1));
    }
}
