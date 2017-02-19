package com.company.watermark.client.command;

import com.company.watermark.client.WatermarkClient;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.List;

/**
 * Hystrix command.
 */

@Slf4j
@Getter
@Setter
public class WatermarkCommand extends BaseCommand<String> {

    private final List<String> watermarkProperties;
    private final WatermarkClient watermarkClient;

    @Builder
    public WatermarkCommand(String groupKey, int timeout, String debugMessage, List<String> watermarkProperties,
                            WatermarkClient watermarkClient) {
        super(groupKey, timeout, debugMessage);
        this.watermarkProperties = watermarkProperties;
        this.watermarkClient = watermarkClient;
    }

    @Override
    protected Observable<String> construct() {
        log.debug("::constructed observable for {}", watermarkProperties);
        return Observable.<String>create(subscriber -> {
                    subscriber.onNext(watermarkClient.createWatermark(watermarkProperties));
                    subscriber.onCompleted();
                }
        ).subscribeOn(Schedulers.computation());
    }

    @Override
    protected Observable<String> resumeWithFallback() {
        log.debug("::resumeWithFallback");
        return Observable.empty();
    }
}
