package com.company.watermark.domain;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import static java.util.Objects.isNull;

/**
 * For a book the watermark includes the properties
 * content, title, author and topic. The journal watermark includes the content, title and author.
 */

@Entity
@Table(name = "watermarks")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Watermark implements Serializable {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id; // to make it different from publication PK and will be used on controller level

    @CreatedDate
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @LastModifiedDate
    @Version
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @OneToOne(mappedBy = "watermark")
    private Publication publication;

    private String status;

    public Status getStatus() {
        return Status.findByName(this.status);
    }

    @AllArgsConstructor
    public enum Status {

        PENDING("PENDING"), FINISHED("FINISHED"), FAILED("FAILED");

        @Getter
        private String name;

        public static Status findByName(String name) {
            if (null == name) {
                return null;
            }

            for (Status status : values()) {
                if (status.getName().equals(name)) {
                    return status;
                }
            }

            return null;
        }
    }

    @Override
    public String toString() {
        return "Watermark[" +
                "id=" + id +
                ", publication=" + (isNull(publication) ? null : publication.getId()) +
                ", status=" + status +
                ']';
    }
}
