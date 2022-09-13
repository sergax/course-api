package com.sergax.courseapi.repository;

import com.sergax.courseapi.model.user.ConfirmationCode;
import com.sergax.courseapi.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationCodeRepository extends JpaRepository<ConfirmationCode, Long> {
    boolean existsByEmailAndStatus(String email, Status status);
    ConfirmationCode getConfirmationCodeByEmailAndStatus(String email, Status status);
}

