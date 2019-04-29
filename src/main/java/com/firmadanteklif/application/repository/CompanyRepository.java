package com.firmadanteklif.application.repository;

import com.firmadanteklif.application.domain.entity.SiteCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<SiteCompany, UUID> {
    Optional<SiteCompany> findByEmail(String email);
}
