package com.ktb.joing.domain.auth.redis;

import org.springframework.data.repository.CrudRepository;

public interface TempUserRepository extends CrudRepository<TempUser, String> {
}
