package com.example.bookstore.inventory.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class KakaoBookResponseDto {
    private Meta meta;
    private List<Document> documents;

    @Getter
    @Setter
    public static class Meta {
        private int total_count;
        private int pageable_count;
        private boolean is_end;
    }

    @Getter
    @Setter
    public static class Document {
        private String title;
        private String contents;
        private String url;
        private String thumbnail;
        private String publisher;
        private List<String> authors;
        private String datetime;
    }
}
