package com.example.forumproject.models;

import java.util.Optional;

public class PostFilterOptions {

    private Optional<String> title;

    private Optional<String> sortBy;

    private Optional<String> sortOrder;

    public PostFilterOptions(String title, String sortBy, String sortOrder) {
        this.title = Optional.ofNullable(title);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getTitle() {
        return title;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }
}
