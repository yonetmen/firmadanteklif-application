package com.firmadanteklif.application.repository;

import com.firmadanteklif.application.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<SiteUser, UUID> {
    Optional<SiteUser> findByEmail(String email);
}
