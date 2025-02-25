package com.example.bookstore.inventory.repository;

import com.example.bookstore.inventory.domain.Inventory;
import com.example.bookstore.inventory.domain.InventoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findInventoryByInventoryId(Long inventoryId);
    List<Inventory> findByStatus(InventoryStatus status);

}

