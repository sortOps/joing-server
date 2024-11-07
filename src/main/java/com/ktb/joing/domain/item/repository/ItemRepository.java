package com.ktb.joing.domain.item.repository;

import com.ktb.joing.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
