package com.company.watermark.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.company.watermark.domain.enums.Content.Values.BOOK_VALUE;
import static com.company.watermark.domain.enums.Content.Values.JOURNAL_VALUE;

@AllArgsConstructor
public enum Content {

    BOOK(BOOK_VALUE), JOURNAL(JOURNAL_VALUE);

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

    //required in order to use enum as discriminator
    public static class Values {
        public static final String BOOK_VALUE = "book";
        public static final String JOURNAL_VALUE = "journal";
    }
}
