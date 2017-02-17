package com.company.watermark.service.impl;

import com.company.watermark.domain.Publication;
import com.company.watermark.domain.Watermark;
import com.company.watermark.domain.enums.Content;
import com.company.watermark.dto.TicketDTO;
import com.company.watermark.service.PublicationService;
import com.company.watermark.service.WatermarkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.UUID;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Transactional
@Slf4j
public class WatermarkServiceImpl implements WatermarkService {

    private final PublicationService publicationService;

    @Override
    public UUID watermarkDocument(Long publicationId, Content content) {
        final Publication publication = publicationService.find(publicationId, content);

        publication.setWatermark(Watermark.builder()
                .publication(publication)
                .build());
        final UUID uuid = publicationService.createOrUpdate(publication).getWatermark().getId();
        log.debug("publication with id={} related to watermark with id={}", publication.getId(), uuid);

        return uuid;
    }

    @Override
    public TicketDTO pollWatermarkStatus(UUID ticketId) {
        return null;
    }
}
