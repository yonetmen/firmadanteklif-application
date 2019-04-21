package com.firmadanteklif.application.repository;

import com.firmadanteklif.application.entity.VerificationCode;
import com.firmadanteklif.application.entity.enums.VerificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VerificationRepository extends JpaRepository<VerificationCode, UUID> {
    Optional<VerificationCode> findByOwnerIdAndVerificationType(UUID ownerId, VerificationType type);
}
