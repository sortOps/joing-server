package com.ktb.joing.domain.user.repository;

import com.ktb.joing.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
