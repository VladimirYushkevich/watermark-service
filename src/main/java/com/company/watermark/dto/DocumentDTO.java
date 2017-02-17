package com.company.watermark.dto;

import lombok.Data;

import java.util.UUID;

/**
 * A document (books, journals) has a title, author and a watermark
 * property. An empty watermark property indicates that the document has not been watermarked yet.
 * @see TicketDTO
 * @see WatermarkDTO
 */

@Data
public class DocumentDTO {
    private UUID ticketId;
    private String title;
    private String author;
    private WatermarkDTO watermark;
}
