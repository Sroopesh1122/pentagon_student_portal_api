package com.pentagon.app.response;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Data;

@Data
public class PageResponse<T> {

    private List<T> content;
    private int currentPage;
    private int totalPages;
    private long totalItems;

    public PageResponse(Page<T> page) {
        this.content = page.getContent();
        this.currentPage = page.getNumber();
        this.totalPages = page.getTotalPages();
        this.totalItems = page.getTotalElements();
    }
}
