package com.example.bookstore.inventory.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class KakaoBookResponseDto {
    private Meta meta;
    private List<Book> documents;

    @Getter
    @Setter
    public static class Meta {
        private boolean is_end;
        private int pageable_count;
        private int total_count;
    }

    @Getter
    @Setter
    public static class Book {
        private String title;
        private String contents;
        private String url;
        private String isbn;
        private LocalDateTime datetime;
        private String[] authors;
        private String publisher;
        private String[] translators;
        private int price;
        private int sale_price;
        private String thumbnail;
        private String status;
    }
}
