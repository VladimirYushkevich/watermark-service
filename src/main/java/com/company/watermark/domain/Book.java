package com.company.watermark.domain;

import lombok.*;

import javax.persistence.Entity;

import static com.company.watermark.domain.Content.BOOK;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Book extends Publication {

    private String topic;

    @Builder
    public Book(final String title, final String author, final Topic topic) {
        super(null, BOOK.getName(), title, author, null, null, null);

        this.topic = topic.getName();
    }

    public Topic getTopic() {
        return Topic.findByName(this.topic);
    }

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
}
