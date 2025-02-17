package com.example.bookstore.inventory.service;

import com.example.bookstore.inventory.client.KakaoBookClient;
import com.example.bookstore.inventory.dto.KakaoBookResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final KakaoBookClient kakaoBookClient;

    public KakaoBookResponseDto searchBooks(String query) {
        return kakaoBookClient.searchBooks(query);
    }
}
