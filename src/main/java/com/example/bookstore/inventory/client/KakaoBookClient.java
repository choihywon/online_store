package com.example.bookstore.inventory.client;

import com.example.bookstore.inventory.dto.KakaoBookResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class KakaoBookClient {

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    @Value("${kakao.api.url}")
    private String kakaoApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public KakaoBookResponseDto searchBooks(String query) {
        String url = UriComponentsBuilder.fromHttpUrl(kakaoApiUrl)
                .queryParam("query", query)
                .queryParam("size", 50)  // ✅ 한 번에 최대 50권 가져오기
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<KakaoBookResponseDto> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, KakaoBookResponseDto.class
        );

        return response.getBody();
    }
}
