package com.ktb.joing.domain.item.repository;

import com.ktb.joing.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByProductManagerUsernameAndCreatedDateTimeGreaterThanEqualOrderByCreatedDateTimeDesc(
            String username, LocalDateTime startDate
    );

    default List<Item> findRecentItems(String username, LocalDateTime startDate) {
        return findByProductManagerUsernameAndCreatedDateTimeGreaterThanEqualOrderByCreatedDateTimeDesc(
                username, startDate
        );
    }
}
