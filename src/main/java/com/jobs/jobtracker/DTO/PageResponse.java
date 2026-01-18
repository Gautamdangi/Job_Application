package com.jobs.jobtracker.DTO;

public record PageResponse<T> (
    java.util.List<T> content,
    int pageNumber,
    int pageSize,
    long totalElements,
    int totalPages,
    boolean isLast
)
{}
