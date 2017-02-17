package com.company.watermark.controller;

import com.company.watermark.domain.Publication;
import com.company.watermark.domain.enums.Content;
import com.company.watermark.dto.PageDTO;
import com.company.watermark.dto.PublicationDTO;
import com.company.watermark.service.PublicationService;
import com.company.watermark.validation.PublicationDTOValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

import static com.company.watermark.utils.PublicationMapper.*;

@RestController
@RequestMapping("/publication")
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Slf4j
public class PublicationController {

    private final PublicationService publicationService;
    private final PublicationDTOValidator validator;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public PublicationDTO createPublication(@Validated @RequestBody PublicationDTO request) {
        log.debug("::createPublication {}", request);

        final Publication publication = publicationService.createOrUpdate(buildPublication(request));

        return buildPublicationDTO(publication);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, params = {"content!="})
    public PageDTO<PublicationDTO> listPublications(Pageable pageable, @RequestParam Content content) {
        log.debug("::listPublications {} with {} content", pageable, content);

        return buildPagePublicationDTO(publicationService.findAllByPage(pageable, content));
    }

    @RequestMapping(value = "/{publication_id}", method = RequestMethod.GET, params = {"content!="})
    public PublicationDTO getPublicationById(@PathVariable("publication_id") Long id, @RequestParam Content content) {
        log.debug("::getById {} with {} content", id, content);

        return buildPublicationDTO(publicationService.find(id, content));
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public PublicationDTO updatePublication(@Validated @RequestBody PublicationDTO request) {
        log.debug("::updatePublication {}", request);

        final Publication publication = publicationService.createOrUpdate(buildPublication(request));

        return buildPublicationDTO(publication);
    }

    @RequestMapping(value = "/{publication_id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePublication(@PathVariable("publication_id") Long id, @RequestParam Content content) {
        log.debug("::deletePublication {} with {} content", id, content);

        publicationService.delete(id, content);
    }
}
