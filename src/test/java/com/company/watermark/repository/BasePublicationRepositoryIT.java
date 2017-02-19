package com.company.watermark.repository;

import com.company.watermark.domain.Content;
import com.company.watermark.domain.Publication;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.company.watermark.RepositoryDataFactory.createPublication;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public abstract class BasePublicationRepositoryIT {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private JournalRepository journalRepository;
    @Autowired
    private TestEntityManager entityManager;

    protected void testPublicationCrudOperations(Content content) {
        final PublicationRepository publicationRepository = resolveRepository(content);

        //given
        final Publication publication = createPublication(content);
        //when
        entityManager.persist(publication);
        //then
        assertThat(publicationRepository.findAll().size(), is(1));
        assertThat(publicationRepository.findOne(publication.getId()), is(publication));

        //given
        publication.setAuthor("newAuthor");
        //when
        entityManager.persist(publication);
        //then
        assertThat(((Publication) publicationRepository.findOne(publication.getId())).getAuthor(), is("newAuthor"));

        //when
        publicationRepository.delete(publication.getId());
        //then
        assertThat(publicationRepository.findAll().size(), is(0));
    }

    public void testPublicationPageable(Content content) throws Exception {
        final PublicationRepository publicationRepository = resolveRepository(content);

        //given
        entityManager.persist(createPublication(content));
        entityManager.persist(createPublication(content));
        entityManager.persist(createPublication(content));

        //when
        final Page<Publication> publicationPage1 = publicationRepository.findAll(new PageRequest(0, 2));
        //then
        assertThat(publicationPage1.getTotalElements(), is(3L));
        assertThat(publicationPage1.getContent().size(), is(2));

        //when
        final Page<Publication> publicationPage2 = publicationRepository.findAll(new PageRequest(1, 2));
        //then
        assertThat(publicationPage2.getTotalElements(), is(3L));
        assertThat(publicationPage2.getContent().size(), is(1));
    }

    private PublicationRepository resolveRepository(Content content) {
        switch (content) {
            case BOOK:
                return bookRepository;
            case JOURNAL:
                return journalRepository;
        }
        return null;
    }
}
