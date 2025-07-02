package dev.umang.userservice_30june.repositories;

import dev.umang.userservice_30june.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);

    Optional<Token> findByTokenAndIsDeletedEqualsAndExpiresAtAfter(String token, boolean deleted, Long expiresAt);
}
