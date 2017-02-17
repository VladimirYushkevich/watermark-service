package com.company.watermark.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Book publications include topics in business, science and media.
 */

@AllArgsConstructor
public enum Topic {

    BUSINESS("Business"), SCIENCE("Science"), MEDIA("Media");

    @Getter
    private String name;

    public static Topic findByName(String name) {
        if (null == name) {
            return null;
        }

        for (Topic topic : values()) {
            if (topic.getName().equals(name)) {
                return topic;
            }
        }

        return null;
    }
}
