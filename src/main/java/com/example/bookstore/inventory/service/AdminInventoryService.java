//package com.example.bookstore.inventory.service;
//
//import com.example.bookstore.inventory.client.KakaoBookClient;
//import com.example.bookstore.inventory.domain.Inventory;
//import com.example.bookstore.inventory.domain.InventoryStatus;
//import com.example.bookstore.inventory.dto.AddInventoryDto;
//import com.example.bookstore.inventory.dto.InventoryForAdminDto;
//import com.example.bookstore.inventory.dto.UpdateInventoryDto;
//import com.example.bookstore.inventory.repository.InventoryRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class AdminInventoryService {
//    private final KakaoBookClient kakaoBookClient;
//    private final InventoryRepository inventoryRepository;
//
//    /** 📌 1. 카카오 API에서 책 검색 */
//    @Transactional(readOnly = true)
//    public Map<String, Object> searchBooks(String query, int page) {
//        return kakaoBookClient.searchBooks(query, page);
//    }
//
//
//
//    private String getAuthenticatedAdmin() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.isAuthenticated()) {
//            return authentication.getName(); // ✅ 로그인한 관리자의 ID 반환
//        }
//        return "UnknownAdmin"; // ✅ 로그인 정보가 없을 경우 기본값
//    }
//
//    /** 📌 2. 책 등록 (createdBy에 관리자 정보 저장) */
//    @Transactional
//    public void save(AddInventoryDto dto) {
//        String authors = (dto.getAuthors() != null) ? String.join(",", dto.getAuthors()) : "";
//        String translators = (dto.getTranslators() != null) ? String.join(",", dto.getTranslators()) : "";
//        String contents = (dto.getContents() != null && !dto.getContents().isEmpty()) ? dto.getContents() : "책 설명이 없습니다.";
//
//        String adminName = getAuthenticatedAdmin(); // ✅ 현재 로그인한 관리자 가져오기
//
//        Inventory inventory = Inventory.builder()
//                .title(dto.getTitle())
//                .isbn(dto.getIsbn())
//                .authors(authors)
//                .publisher(dto.getPublisher())
//                .translators(translators)
//                .salePrice(dto.getSalePrice())
//                .thumbnail(dto.getThumbnail())
//                .quantity(dto.getQuantity())
//                .contents(contents)
//                .status(InventoryStatus.ON_SALES) // 기본 상태를 ON_SALES로 설정
//                .createdAt(LocalDateTime.now()) // ✅ 현재 시간 설정
//                .lastModifiedAt(LocalDateTime.now()) // ✅ 현재 시간 설정
//                .createdBy(adminName) // ✅ 로그인한 관리자 정보 저장
//                .lastModifiedBy(adminName) // ✅ 로그인한 관리자 정보 저장
//                .build();
//
//        inventoryRepository.save(inventory);
//    }
//
//    @Transactional(readOnly = true)
//    public List<InventoryForAdminDto> findAll() {
//        return inventoryRepository.findAll().stream().map(book ->
//                new InventoryForAdminDto(
//                        book.getInventoryId(),
//                        book.getTitle(),
//                        book.getIsbn(),
//                        book.getAuthors().split(","), // ✅ String → 배열 변환
//                        book.getPublisher(),
//                        book.getCreatedAt(),
//                        book.getLastModifiedAt(),
//                        book.getCreatedBy(),
//                        book.getLastModifiedBy(),
//                        book.getPrice(),
//                        book.getSalePrice(),
//                        book.getThumbnail(),
//                        book.getQuantity(),
//                        book.getStatus(),
//                        book.getTranslators().split(",") // ✅ String → 배열 변환
//                )).collect(Collectors.toList());
//    }
//
//    /** 📌 4. 책 상세 조회 */
//    @Transactional(readOnly = true)
//    public InventoryForAdminDto findById(Long id) {
//        Inventory book = inventoryRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("해당 책이 존재하지 않습니다."));
//
//        return new InventoryForAdminDto(
//                book.getInventoryId(),
//                book.getTitle(),
//                book.getIsbn(),
//                book.getAuthors().split(","), // String → 배열 변환
//                book.getPublisher(),
//                book.getCreatedAt(),  // ✅ 추가
//                book.getLastModifiedAt(),  // ✅ 추가
//                book.getCreatedBy(),  // ✅ 추가
//                book.getLastModifiedBy(),  // ✅ 추가
//                book.getPrice(),
//                book.getSalePrice(),
//                book.getThumbnail(),
//                book.getQuantity(),
//                book.getStatus(),
//                book.getTranslators().split(",") // String → 배열 변환
//        );
//    }
//
//
//    /** 📌 5. 책 수정 */
//    @Transactional
//    public void update(UpdateInventoryDto dto) {
//        Inventory book = inventoryRepository.findById(dto.getInventoryId())
//                .orElseThrow(() -> new IllegalArgumentException("책을 찾을 수 없습니다."));
//        book.updateQuantity(dto.getQuantity());
//        book.updateStatus(dto.getStatus());
//    }
//}
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

    /** 📌 1. 카카오 API에서 책 검색 */
    @Transactional(readOnly = true)
    public Map<String, Object> searchBooks(String query, int page) {
        return kakaoBookClient.searchBooks(query, page);
    }



    private String getAuthenticatedAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName(); // ✅ 로그인한 관리자의 ID 반환
        }
        return "UnknownAdmin"; // ✅ 로그인 정보가 없을 경우 기본값
    }

    @Transactional
    public void save(AddInventoryDto dto) {
        System.out.println("✅ 저장할 ISBN: " + dto.getIsbn());

        String authors = (dto.getAuthors() != null) ? String.join(",", dto.getAuthors()) : "";
        String translators = (dto.getTranslators() != null) ? String.join(",", dto.getTranslators()) : "";
        String contents = (dto.getContents() != null && !dto.getContents().isEmpty()) ? dto.getContents() : "책 설명이 없습니다.";
        String adminName = getAuthenticatedAdmin(); // ✅ 현재 로그인한 관리자 가져오기

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
                .createdAt(LocalDateTime.now()) // ✅ 현재 시간 추가
                .lastModifiedAt(LocalDateTime.now()) // ✅ 현재 시간 추가
                .createdBy(adminName) // ✅ 로그인한 관리자 정보 저장
                .lastModifiedBy(adminName) // ✅ 로그인한 관리자 정보 저장
                .build();

        inventoryRepository.save(inventory);
    }



    @Transactional(readOnly = true)
    public List<InventoryForAdminDto> findAll() {
        return inventoryRepository.findAll().stream().map(book ->
                new InventoryForAdminDto(
                        book.getInventoryId(),
                        book.getTitle(),
                        book.getIsbn(),
                        book.getAuthors().split(","), // ✅ String → 배열 변환
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
                        book.getTranslators().split(",") // ✅ String → 배열 변환
                )).collect(Collectors.toList());
    }

    /** 📌 4. 책 상세 조회 */
    @Transactional(readOnly = true)
    public InventoryForAdminDto findById(Long id) {
        Inventory book = inventoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 책이 존재하지 않습니다."));

        return new InventoryForAdminDto(
                book.getInventoryId(),
                book.getTitle(),
                book.getIsbn(),
                book.getAuthors().split(","), // String → 배열 변환
                book.getPublisher(),
                book.getCreatedAt(),  // ✅ 추가
                book.getLastModifiedAt(),  // ✅ 추가
                book.getCreatedBy(),  // ✅ 추가
                book.getLastModifiedBy(),  // ✅ 추가
                book.getPrice(),
                book.getSalePrice(),
                book.getThumbnail(),
                book.getQuantity(),
                book.getStatus(),
                book.getTranslators().split(",") // String → 배열 변환
        );
    }


    /** 📌 5. 책 수정 */
    @Transactional
    public void update(UpdateInventoryDto dto) {
        Inventory book = inventoryRepository.findById(dto.getInventoryId())
                .orElseThrow(() -> new IllegalArgumentException("책을 찾을 수 없습니다."));
        book.updateQuantity(dto.getQuantity());
        book.updateStatus(dto.getStatus());
    }
}
