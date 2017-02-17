package com.company.watermark.dto;

import java.util.UUID;

/**
 * Class for watermark presentation. For a book the watermark includes the properties
 * content, title, author and topic. The journal watermark includes the content, title and author.
 * On DTO (presentation layer) to avoid duplicate code this class can be extended from publication presentation.
 *
 * @see PublicationDTO
 */

public class WatermarkDTO extends PublicationDTO {
    private UUID id;

}
