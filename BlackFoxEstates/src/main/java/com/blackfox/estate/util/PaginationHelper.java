package com.blackfox.estate.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.awt.print.Pageable;
import java.util.Arrays;
import java.util.List;

public class PaginationHelper {
    public static Pageable createPageRequest(int page, int size, String[] sort) {
        List<Sort.Order> orders = Arrays.stream(sort)
                .map(PaginationHelper::toOrder)
                .toList();
        return (Pageable) PageRequest.of(page, size, Sort.by(orders));
    }

    private static Sort.Order toOrder(String sort) {
        String[] parts = sort.split(",");
        return new Sort.Order(Sort.Direction.fromString(parts[1]), parts[0]);
    }
}
