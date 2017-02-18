package com.company.watermark.utils;

import com.company.watermark.domain.Book;
import com.company.watermark.domain.Content;
import com.company.watermark.domain.Journal;
import com.company.watermark.domain.Publication;
import com.company.watermark.dto.PageDTO;
import com.company.watermark.dto.PublicationDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

import static com.company.watermark.domain.Content.BOOK;
import static java.util.stream.Collectors.toList;
import static org.springframework.beans.BeanUtils.copyProperties;

/**
 * Utility class for mapping publication to related DTOs and vice versa.
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PublicationMapper {

    public static Publication buildPublication(PublicationDTO dto) {
        Publication publication = null;

        final Content content = dto.getContent();
        switch (content) {
            case BOOK:
                publication = new Book();
                final Book.Topic topic = dto.getTopic();
                copyProperties(dto, publication);
                ((Book) publication).setTopic(topic.getName());
                break;
            case JOURNAL:
                publication = new Journal();
                copyProperties(dto, publication);
                break;
        }

        publication.setContent(content);

        return publication;
    }

    public static PublicationDTO buildPublicationDTO(Publication publication) {
        PublicationDTO dto = new PublicationDTO();

        copyProperties(publication, dto);
        dto.setContent(publication.getContent());
        if (BOOK.equals(publication.getContent())) {
            dto.setTopic(((Book) publication).getTopic());
        }

        return dto;
    }

    public static PageDTO<PublicationDTO> buildPagePublicationDTO(Page<Publication> publicationPage) {
        final List<PublicationDTO> entries = publicationPage.getContent().parallelStream()
                .map(PublicationMapper::buildPublicationDTO)
                .collect(toList());

        return PageDTO.<PublicationDTO>builder()
                .totalPages(publicationPage.getTotalPages())
                .totalEntries(publicationPage.getTotalElements())
                .entries(entries)
                .build();
    }
}
