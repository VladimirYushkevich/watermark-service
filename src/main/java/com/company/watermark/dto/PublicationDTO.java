package com.company.watermark.dto;

import com.company.watermark.domain.Book;
import com.company.watermark.domain.Content;
import com.company.watermark.domain.Journal;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Class for publication presentation. Base class for (book, journal) and watermark properties.
 *
 * @see Book
 * @see Journal
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PublicationDTO {
    private Long id;
    @NotNull
    private Content content;
    @NotNull
    private String title;
    @NotNull
    private String author;
    private Book.Topic topic;

}