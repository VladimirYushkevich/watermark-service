package com.company.watermark.utils;

import com.company.watermark.domain.Publication;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Utility class to create watermark property (String).
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WatermarkGenerator {

    public static String generateWatermark(Publication publication) {
        final List<String> watermarkProperties = publication.getWatermarkProperties();

        StringBuilder watermark = new StringBuilder();
        watermark.append("---*");
        watermarkProperties.forEach(wp -> watermark.append(wp).append("*"));
        watermark.append("---");

        return watermark.toString();
    }

}
