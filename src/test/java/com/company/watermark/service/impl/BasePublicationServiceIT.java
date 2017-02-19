package com.company.watermark.service.impl;

import com.company.watermark.client.WatermarkClient;
import com.company.watermark.domain.Content;
import com.company.watermark.domain.Publication;
import com.company.watermark.domain.Watermark;
import com.company.watermark.exception.NotFoundException;
import com.company.watermark.service.PublicationService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.company.watermark.RepositoryDataFactory.createPublication;
import static com.company.watermark.domain.Watermark.Status.*;
import static com.company.watermark.utils.WatermarkGenerator.generateWatermark;
import static java.util.Objects.isNull;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ComponentScan("com.company.watermark.service.impl")
@DataJpaTest
public abstract class BasePublicationServiceIT {

    @Autowired
    protected PublicationService publicationService;
    @MockBean
    private WatermarkClient watermarkClient;

    protected void testPublicationCrudOperations(Content content, String watermarkProperty) {
        //create
        //when
        final Publication publication = publicationService.createOrUpdate(createPublication(content));
        //then
        final Long id = publication.getId();
        assertNotNull(id);

        //when
        final Watermark watermark = publicationService.find(id, content).getWatermark();
        //then
        assertThat(watermark.getPublication(), is(publication));

        //update
        //given
        watermark.setProperty(generateWatermark(watermark.getPublication().getWatermarkProperties()));
        //when
        final String property = publicationService.find(id, content).getWatermark().getProperty();
        //then
        assertThat(property, is(watermarkProperty));

        //delete
        //when
        publicationService.delete(id, content);
        //then
        try {
            publicationService.find(id, content);
            fail("Should throw exception");
        } catch (NotFoundException ignored) {
        }

        //pageable
        //given
        publicationService.createOrUpdate(createPublication(content));
        publicationService.createOrUpdate(createPublication(content));
        publicationService.createOrUpdate(createPublication(content));

        //when
        final Page<Publication> journalPage1 = publicationService.findAllByPage(new PageRequest(0, 2), content);
        //then
        assertThat(journalPage1.getTotalElements(), is(3L));
        assertThat(journalPage1.getContent().size(), is(2));

        //when
        final Page<Publication> journalPage2 = publicationService.findAllByPage(new PageRequest(1, 2), content);
        //then
        assertThat(journalPage2.getTotalElements(), is(3L));
        assertThat(journalPage2.getContent().size(), is(1));
    }

    protected void testWatermarkSetUp(Content content) {
        //given
        Publication publication = createPublication(content);
        publication.setWatermark(null);
        final Publication publicationWithoutWatermark = publicationService.createOrUpdate(publication);
        isNull(publicationWithoutWatermark.getWatermark());
        //when
        final Watermark watermark = publicationService.setWatermark(publicationWithoutWatermark.getId(), content);
        //then
        assertThat(publicationService.find(publication.getId(), content).getWatermark(), is(watermark));

        //given
        final Publication publicationWithNewWatermark = publicationService.createOrUpdate(createPublication(content));
        assertThat(publicationWithNewWatermark.getWatermark().getStatus(), is(NEW));
        //when
        publicationService.setWatermark(publicationWithNewWatermark.getId(), content);
        //then
        assertThat(publicationService.find(publicationWithNewWatermark.getId(), content).getWatermark().getStatus(), is(PENDING));

        //given
        publication = createPublication(content);
        publication.getWatermark().setStatus(FAILED.getName());
        Publication publicationWithFailedWatermark = publicationService.createOrUpdate(publication);
        assertThat(publicationService.find(publication.getId(), content).getWatermark().getStatus(), is(FAILED));
        //when
        publicationService.setWatermark(publicationWithFailedWatermark.getId(), content);
        //then
        assertThat(publicationService.find(publicationWithFailedWatermark.getId(), content).getWatermark().getStatus(), is(PENDING));

        //given
        publication = createPublication(content);
        publication.getWatermark().setStatus(PENDING.getName());
        final Publication publicationWithPendingWatermark = publicationService.createOrUpdate(publication);
        //when
        publicationService.setWatermark(publicationWithPendingWatermark.getId(), content);
        //then
        fail("Should throw exception");
    }

    protected void testWatermarkStatusUpdateForPublication(Content content) {
        //given
        Publication publication = publicationService.createOrUpdate(createPublication(content));
        //when
        publicationService.updateWatermarkStatus(publication, "");
        //then
        assertThat(publicationService.find(publication.getId(), content).getWatermark().getStatus(), is(FAILED));

        //given
        publication = publicationService.createOrUpdate(createPublication(content));
        //when
        publicationService.updateWatermarkStatus(publication, "someProperty");
        //then
        assertThat(publicationService.find(publication.getId(), content).getWatermark().getStatus(), is(SUCCESS));
    }
}
