package com.company.watermark.dto;

import com.company.watermark.domain.Watermark;
import lombok.Data;

/**
 * A document (books, journals) has a title, author and a watermark
 * property. An empty watermark property indicates that the document has not been watermarked yet.
 */

@Data
public class DocumentDTO {
    private String title;
    private String author;
    private Watermark watermark;
}
