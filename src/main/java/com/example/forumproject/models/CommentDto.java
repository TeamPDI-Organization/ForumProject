package com.example.forumproject.models;

import jakarta.validation.constraints.NotNull;

public class CommentDto {

    @NotNull(message = "Content must not be null")
    private String content;

    public CommentDto() {

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
