package com.example.bookstore.inventory.repository;

import com.example.bookstore.inventory.domain.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findInventoryByInventoryId(Long inventoryId);
}
