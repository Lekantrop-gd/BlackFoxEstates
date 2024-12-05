package com.blackfox.estate.util;

import org.springframework.data.domain.Sort;

public class SortUtils {

    public static Sort createSort(String[] sort) {
        if (sort.length == 2) {
            String field = sort[0];
            String direction = sort[1];
            return direction.equalsIgnoreCase("desc") ? Sort.by(field).descending() : Sort.by(field).ascending();
        }
        return Sort.by("id").ascending();
    }
}