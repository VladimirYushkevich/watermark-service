package com.company.watermark.domain;

import lombok.*;

import javax.persistence.Entity;

import static com.company.watermark.domain.Content.JOURNAL;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Journal extends Publication {

    @Builder
    public Journal(final String title, final String author) {
        super(null, JOURNAL.getName(), title, author, null, null, null);
    }
}
