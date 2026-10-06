package com.glimpse.gpm.dto.common;

import org.springframework.data.domain.Page;

public record PageResponse<T>(
        java.util.List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {
    public static final int DEFAULT_SIZE = 10;
    public static final int MAX_SIZE = 100;

    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }

    public static int normalizePage(Integer page) {
        return page == null ? 0 : Math.max(page, 0);
    }

    public static int normalizeSize(Integer size) {
        return size == null ? DEFAULT_SIZE : Math.min(Math.max(size, 1), MAX_SIZE);
    }
}
