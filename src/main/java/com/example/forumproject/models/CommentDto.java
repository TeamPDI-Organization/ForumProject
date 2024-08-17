package com.example.forumproject.models;

import jakarta.validation.constraints.NotNull;

public class CommentDto {

    private int id;

    @NotNull(message = "Content must not be null")
    private String content;

    public CommentDto() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
