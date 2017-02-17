package com.company.watermark.service;

import com.company.watermark.domain.enums.Content;
import com.company.watermark.dto.TicketDTO;

import java.util.UUID;

/**
 * Watermark service for creation/monitoring of watermarks and also for transforming data for presentation layer.
 */

public interface WatermarkService {

    /**
     * For a given content document (publication) the service should
     * return a ticket, which can be used to poll the status of processing.
     *
     * @param publicationId Id of PublicationDTO (Document Id)
     * @param content       Content of document
     * @return UUID of Ticket
     */
    UUID watermarkDocument(Long publicationId, Content content);

    /**
     * Polls status of watermark. If the watermarking is finished the
     * document can be retrieved with the ticket.
     *
     * @param ticketId Id of ticket/watermark
     * @return Ticket with optional document
     */
    TicketDTO pollWatermarkStatus(UUID ticketId);
}
