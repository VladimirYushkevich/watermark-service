package com.company.watermark.service.impl;

import com.company.watermark.client.WatermarkClient;
import com.company.watermark.client.command.WatermarkCommand;
import com.company.watermark.domain.Content;
import com.company.watermark.domain.Publication;
import com.company.watermark.domain.Watermark;
import com.company.watermark.exception.WatermarkException;
import com.company.watermark.repository.WatermarkRepository;
import com.company.watermark.service.PublicationService;
import com.company.watermark.service.WatermarkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rx.Observable;

import javax.inject.Inject;
import java.util.UUID;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Transactional
@Slf4j
public class WatermarkServiceImpl implements WatermarkService {

    private final WatermarkRepository watermarkRepository;
    private final PublicationService publicationService;
    private final WatermarkClient watermarkClient;

    @Value("${hystrix.command.WatermarkCommand.timeoutInMilliseconds}")
    private int watermarkTimeOut;
    @Value("${hystrix.command.watermark.groupKey}")
    private String watermarkGroupKey;

    @Override
    public Observable<UUID> watermarkDocument(Long publicationId, Content content) {
        return Observable.create(subscriber -> {
                    subscriber.onNext(getWatermarkUuid(publicationId, content));
                    subscriber.onCompleted();
                    subscriber.onError(
                            new WatermarkException(String.format("can't watermark document [%s/%s]", publicationId, content)));
                }
        );
    }

    @Override
    public Observable<Watermark> pollWatermarkStatus(UUID ticketId) {
        return Observable.create(subscriber -> {
                    subscriber.onNext(watermarkRepository.findById(ticketId));
                    subscriber.onCompleted();
                    subscriber.onError(
                            new WatermarkException(String.format("can't retrieve watermark with id=%s", ticketId)));
                }
        );
    }

    /**
     * Retrieves UUID of Watermark and executes watermark generation via hystrix command({@link WatermarkCommand}).
     *
     * @param publicationId Publication id
     * @param content       Publication content
     * @return UUID of Watermark
     * @see Watermark
     * @see Publication
     */
    private UUID getWatermarkUuid(Long publicationId, Content content) {
        log.debug("::getWatermarkUuid [{}/{}]", publicationId, content);

        final Watermark createdWatermark = publicationService.setWatermark(publicationId, content);

        new WatermarkCommand(watermarkGroupKey, watermarkTimeOut, "watermarkDocument",
                createdWatermark.getPublication().getWatermarkProperties(), watermarkClient).observe()
                .subscribe(o -> publicationService.updateWatermarkStatus(createdWatermark.getPublication(), o));

        return createdWatermark.getId();
    }
}
