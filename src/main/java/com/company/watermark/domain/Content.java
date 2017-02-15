package com.company.watermark.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Content {

    BOOK("book"), JOURNAL("journal");

    @Getter
    private String name;

    public static Content findByName(String name) {
        if (null == name) {
            return null;
        }

        for (Content content : values()) {
            if (content.getName().equals(name)) {
                return content;
            }
        }

        return null;
    }
}
