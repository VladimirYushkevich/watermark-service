package com.company.watermark.controller;

import com.company.watermark.dto.PublicationRequestDTO;
import com.company.watermark.dto.TicketDTO;
import com.company.watermark.service.WatermarkService;
import com.company.watermark.utils.mapper.WatermarkMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.inject.Inject;
import java.util.UUID;

/**
 * Async controller for creation of watermark and for polling it\ status.
 */

@RestController
@RequestMapping("/watermark")
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Slf4j
public class WatermarkController {

    private final WatermarkService watermarkService;

    @RequestMapping(method = RequestMethod.POST)
    public DeferredResult<UUID> watermarkDocument(@Validated @RequestBody PublicationRequestDTO request) {
        log.debug("::watermarkDocument {}", request);

        DeferredResult<UUID> deferredResult = new DeferredResult<>();
        watermarkService.watermarkDocument(request.getPublicationId(), request.getContent())
                .subscribe(deferredResult::setResult);

        return deferredResult;
    }

    @RequestMapping(value = "/{ticket_id}", method = RequestMethod.GET, params = {"ticket_id!="})
    public DeferredResult<TicketDTO> getTicketById(@PathVariable("ticket_id") UUID ticketId) {
        log.debug("::getTicketById {}", ticketId);

        DeferredResult<TicketDTO> deferredResult = new DeferredResult<>();
        watermarkService.pollWatermarkStatus(ticketId)
                .map(WatermarkMapper::buildTicketDTO)
                .subscribe(deferredResult::setResult);


        return deferredResult;
    }
}
