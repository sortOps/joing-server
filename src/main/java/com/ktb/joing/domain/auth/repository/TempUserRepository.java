package com.ktb.joing.domain.auth.repository;

import com.ktb.joing.domain.auth.entity.TempUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TempUserRepository extends JpaRepository<TempUser, String> {
}
