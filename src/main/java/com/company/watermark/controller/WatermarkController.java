package com.company.watermark.controller;

import com.company.watermark.dto.PublicationRequestDTO;
import com.company.watermark.service.WatermarkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.inject.Inject;
import java.util.UUID;

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
}
