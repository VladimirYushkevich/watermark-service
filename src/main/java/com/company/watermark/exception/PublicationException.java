package com.company.watermark.exception;

import com.company.watermark.domain.Content;

import java.io.Serializable;

public class PublicationException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -3378549727827308385L;

    public PublicationException(Content content) {
        super(String.format("Error for publication with content [%s]", content));
    }
}
