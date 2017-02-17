package com.company.watermark.dto;

import com.company.watermark.domain.enums.Content;
import com.company.watermark.domain.enums.Topic;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Class for publication presentation. Base class for (book, journal) and watermark properties.
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
    private Topic topic;

}