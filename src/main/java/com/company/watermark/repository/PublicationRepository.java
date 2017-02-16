package com.company.watermark.repository;

import com.company.watermark.domain.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface PublicationRepository<T extends Publication> extends JpaRepository<T, Long> {
}
