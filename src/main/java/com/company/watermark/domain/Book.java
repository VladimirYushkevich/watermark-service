package com.company.watermark.domain;

import com.company.watermark.domain.enums.Topic;
import com.google.common.collect.Lists;
import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

import static com.company.watermark.domain.enums.Content.BOOK;
import static com.company.watermark.domain.enums.Content.Values.BOOK_VALUE;

/**
 * Entity class for Book.
 *
 * @see Publication
 */

@Entity
@DiscriminatorValue(value = BOOK_VALUE)
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

    @Override
    public List<String> getWatermarkProperties() {
        final List<String> superProperties = Lists.newArrayList(super.getWatermarkProperties());
        superProperties.add(topic);
        return superProperties;
    }

    public Topic getTopic() {
        return Topic.findByName(this.topic);
    }
}
