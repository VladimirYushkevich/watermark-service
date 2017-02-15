package com.company.watermark.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static java.util.Objects.isNull;

/**
 * Base class to support inheritance.
 */

@Entity
@Table(name = "publications")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "content", discriminatorType = DiscriminatorType.STRING)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class Publication implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "content", insertable = false, updatable = false)
    private String discriminator;

    private String title;

    private String author;

    @CreatedDate
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @LastModifiedDate
    @Version
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "watermark_id")
    private Watermark watermark;

    public Content getContent() {
        return Content.findByName(this.discriminator);
    }

    @Override
    public String toString() {
        return "Publication[" +
                "id=" + id +
                ", discriminator='" + discriminator + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", watermark=" + (isNull(watermark) ? null : String.format("[%s/%s]", watermark.getId(), watermark.getStatus())) +
                ']';
    }
}
