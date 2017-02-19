package com.company.watermark.service.impl;

import com.company.watermark.client.WatermarkClient;
import com.company.watermark.client.command.WatermarkCommand;
import com.company.watermark.domain.Content;
import com.company.watermark.domain.Publication;
import com.company.watermark.domain.Watermark;
import com.company.watermark.exception.NotFoundException;
import com.company.watermark.exception.WatermarkException;
import com.company.watermark.service.PublicationService;
import com.company.watermark.service.WatermarkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rx.Observable;

import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;

import static com.company.watermark.domain.Watermark.Status.SUCCESS;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Transactional
@Slf4j
public class WatermarkServiceImpl implements WatermarkService {

    private final PublicationService publicationService;
    private final WatermarkClient watermarkClient;

    @Value("${hystrix.command.WatermarkCommand.timeoutInMilliseconds}")
    private int watermarkTimeOut;
    @Value("${hystrix.command.watermark.groupKey}")
    private String watermarkGroupKey;

    @Override
    public Observable<UUID> watermarkDocument(Long publicationId, Content content) {
        return Observable.create(subscriber -> {
                    subscriber.onNext(getObservableUuid(publicationId, content));
                    subscriber.onCompleted();
                    subscriber.onError(
                            new WatermarkException(String.format("can't watermark document [%s/%s]", publicationId, content)));
                }
        );
    }

    private UUID getObservableUuid(Long publicationId, Content content) {
        log.debug("::getObservableUuid [{}/{}]", publicationId, content);

        final Watermark createdWatermark = publicationService.setWatermark(publicationId, content);

        new WatermarkCommand(watermarkGroupKey, watermarkTimeOut, "watermarkDocument",
                createdWatermark.getPublication().getWatermarkProperties(), watermarkClient).observe()
                .subscribe(o -> updateWatermark(o, publicationId, content));

        log.debug("!!!!! continue");
        return createdWatermark.getId();
    }

    @Override
    public Publication pollWatermarkStatus(UUID ticketId) {
        return null;
    }

    private void updateWatermark(String watermarkProperty, Long publicationId, Content content) {
        log.debug("::updateWatermark [{}/{}/{}]", publicationId, content, watermarkProperty);

        final Publication publication = Optional.ofNullable(publicationService.find(publicationId, content))
                .orElseThrow(NotFoundException::new);
        publication.getWatermark().setProperty(watermarkProperty);
        publication.getWatermark().setStatus(SUCCESS.getName());

        publicationService.createOrUpdate(publication);
    }
}
