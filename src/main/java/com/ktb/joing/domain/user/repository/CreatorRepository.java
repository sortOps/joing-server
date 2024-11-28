package com.ktb.joing.domain.user.repository;

import com.ktb.joing.domain.user.entity.Creator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CreatorRepository extends JpaRepository<Creator, Long> {
    Optional<Creator> findByUsername(String username);
}
