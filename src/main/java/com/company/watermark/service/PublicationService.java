package com.company.watermark.service;

import com.company.watermark.domain.Content;
import com.company.watermark.domain.Publication;
import com.company.watermark.domain.Watermark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Simple CRUD operations for Books/Journals.
 */

public interface PublicationService {

    Publication createOrUpdate(Publication publication);

    void delete(Long publicationId, Content content);

    Publication find(Long publicationId, Content content);

    Page<Publication> findAllByPage(Pageable pageable, Content content);

    Watermark setWatermark(Long publicationId, Content content);
}
