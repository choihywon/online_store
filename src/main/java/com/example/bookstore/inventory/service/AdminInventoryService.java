
package com.example.bookstore.inventory.service;

import com.example.bookstore.inventory.client.KakaoBookClient;
import com.example.bookstore.inventory.domain.Inventory;
import com.example.bookstore.inventory.domain.InventoryStatus;
import com.example.bookstore.inventory.dto.AddInventoryDto;
import com.example.bookstore.inventory.dto.InventoryForAdminDto;
import com.example.bookstore.inventory.dto.UpdateInventoryDto;
import com.example.bookstore.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminInventoryService {
    private final KakaoBookClient kakaoBookClient;
    private final InventoryRepository inventoryRepository;


    @Transactional(readOnly = true)
    public Map<String, Object> searchBooks(String query, int page) {
        return kakaoBookClient.searchBooks(query, page);
    }



    private String getAuthenticatedAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return "UnknownAdmin";
    }

    @Transactional
    public void save(AddInventoryDto dto) {
        System.out.println(" service ISBN: " + dto.getIsbn());

        String authors = (dto.getAuthors() != null) ? String.join(",", dto.getAuthors()) : "";
        String translators = (dto.getTranslators() != null) ? String.join(",", dto.getTranslators()) : "";
        String contents = (dto.getContents() != null && !dto.getContents().isEmpty()) ? dto.getContents() : "책 설명이 없습니다.";
        String adminName = getAuthenticatedAdmin();

        Inventory inventory = Inventory.builder()
                .title(dto.getTitle())
                .isbn(dto.getIsbn())
                .authors(authors)
                .publisher(dto.getPublisher())
                .translators(translators)
                .salePrice(dto.getSalePrice())
                .thumbnail(dto.getThumbnail())
                .quantity(dto.getQuantity())
                .contents(contents)
                .datetime(dto.getDatetime())
                .status(InventoryStatus.ON_SALES)
                .createdAt(LocalDateTime.now())
                .lastModifiedAt(LocalDateTime.now())
                .createdBy(adminName)
                .lastModifiedBy(adminName)
                .build();

        inventoryRepository.save(inventory);
    }
    @Transactional(readOnly = true)
    public InventoryForAdminDto findById(Long id) {
        System.out.println("Service - 전달받은 ID: " + id);

        if (id == null) {
            throw new IllegalArgumentException("조회할 ID 확인 없음");
        }

        Inventory book = inventoryRepository.findInventoryByInventoryId(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 책이 존재하지 않습니다."));

        return new InventoryForAdminDto(
                book.getInventoryId(),
                book.getTitle(),
                book.getIsbn(),
                book.getAuthors() != null ? book.getAuthors().split(",") : new String[]{},
                book.getPublisher(),
                book.getCreatedAt(),
                book.getLastModifiedAt(),
                book.getCreatedBy(),
                book.getLastModifiedBy(),
                book.getPrice(),
                book.getSalePrice(),
                book.getThumbnail(),
                book.getQuantity(),
                book.getStatus(),
                book.getTranslators() != null ? book.getTranslators().split(",") : new String[]{}
        );
    }



    @Transactional(readOnly = true)
    public List<InventoryForAdminDto> findAll() {
        return inventoryRepository.findAll().stream().map(book ->
                new InventoryForAdminDto(
                        book.getInventoryId(),
                        book.getTitle(),
                        book.getIsbn(),
                        book.getAuthors().split(","),
                        book.getPublisher(),
                        book.getCreatedAt(),
                        book.getLastModifiedAt(),
                        book.getCreatedBy(),
                        book.getLastModifiedBy(),
                        book.getPrice(),
                        book.getSalePrice(),
                        book.getThumbnail(),
                        book.getQuantity(),
                        book.getStatus(),
                        book.getTranslators().split(",")
                )).collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public Inventory findInventoryByInventoryId(Long id) {
        System.out.println("Service - 전달받은 ID: " + id);

        if (id == null) {
            throw new IllegalArgumentException("조회할 ID 없음");
        }

        return inventoryRepository.findInventoryByInventoryId(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 책이 존재하지 않습니다."));
    }

    @Transactional
    public void update(UpdateInventoryDto dto) {
        System.out.println("Service - 수정 요청 ID: " + dto.getInventoryId());

        if (dto.getInventoryId() == null) {
            throw new IllegalArgumentException("책의 ID");
        }

        Inventory book = findInventoryByInventoryId(dto.getInventoryId());


        book.updateQuantity(dto.getQuantity());
        book.updateStatus(dto.getStatus());

    }



}
