package com.ktb.joing.domain.user.repository;

import com.ktb.joing.domain.user.entity.ProductManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductManagerRepository extends JpaRepository<ProductManager, Long> {
    Optional<ProductManager> findByUsername(String username);
}
