package com.company.watermark.dto;

import lombok.Data;

import java.util.UUID;

/**
 * Ticket class used for watermark polling. This entity is in one-to-one relation with
 * {@link com.company.watermark.domain.Watermark}
 * (Ticket {@link #id} is a primary key in <b>Watermark</b>).
 * If the watermarking is finished the
 * document can be retrieved with the ticket.
 * @see DocumentDTO
 */

@Data
public class TicketDTO {
    private UUID id;
    private DocumentDTO document;
}
