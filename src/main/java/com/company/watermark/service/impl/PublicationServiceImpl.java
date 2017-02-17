package com.company.watermark.service.impl;

import com.company.watermark.domain.Publication;
import com.company.watermark.domain.enums.Content;
import com.company.watermark.exception.NotFoundException;
import com.company.watermark.exception.PublicationException;
import com.company.watermark.repository.BookRepository;
import com.company.watermark.repository.JournalRepository;
import com.company.watermark.repository.PublicationRepository;
import com.company.watermark.service.PublicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Optional;

import static java.util.Objects.isNull;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Transactional
@Slf4j
public class PublicationServiceImpl implements PublicationService {

    private final BookRepository bookRepository;
    private final JournalRepository journalRepository;

    @Override
    public Publication createOrUpdate(Publication publication) {
        final Long id = publication.getId();
        final Publication createdPublication = (Publication) resolveRepository(publication.getContent()).save(publication);
        log.debug("{} {}", isNull(id) ? "created" : "updated", createdPublication);
        return createdPublication;
    }

    @Override
    public Publication find(Long publicationId, Content content) {
        final Publication publication = Optional.ofNullable(resolveRepository(content).findById(publicationId))
                .orElseThrow(NotFoundException::new);
        log.debug("found {}", publication);
        return publication;
    }

    @Override
    public void delete(Long publicationId, Content content) {
        final PublicationRepository publicationRepository = resolveRepository(content);

        Optional.ofNullable(publicationRepository.findById(publicationId))
                .orElseThrow(NotFoundException::new);

        //noinspection unchecked
        publicationRepository.delete(publicationId);

        log.debug("deleted publication with id = {}", publicationId);
    }

    @Override
    public Page<Publication> findAllByPage(Pageable pageable, Content content) {
        final Page page = resolveRepository(content).findAll(pageable);
        log.debug("page {} from {} has {} publications", page.getNumber(), page.getTotalElements(), page.getContent().size());
        return page;
    }

    private PublicationRepository resolveRepository(Content content) {
        switch (content) {
            case BOOK:
                return bookRepository;
            case JOURNAL:
                return journalRepository;
            default:
                log.error("Content not resolved for {}", content);
                throw new PublicationException(content);
        }
    }
}