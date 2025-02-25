package com.example.bookstore.inventory.service;

import com.example.bookstore.inventory.domain.InventoryStatus;
import com.example.bookstore.inventory.dto.InventoryForUserDto;
import com.example.bookstore.inventory.repository.InventoryRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserInventoryService {

    private final InventoryRepository inventoryRepository;


    @Autowired
    public UserInventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional(readOnly = true)
    public List<InventoryForUserDto> findAllOnSaleBooks() {
        return inventoryRepository.findByStatus(InventoryStatus.ON_SALES).stream()
                .map(book -> new InventoryForUserDto(
                        book.getInventoryId(),
                        book.getTitle(),
                        book.getIsbn(),
                        book.getAuthors() != null ? book.getAuthors().split(",") : new String[]{},
                        book.getPublisher(),
                        book.getSalePrice(),
                        book.getThumbnail()
                ))
                .collect(Collectors.toList());
    }
}
