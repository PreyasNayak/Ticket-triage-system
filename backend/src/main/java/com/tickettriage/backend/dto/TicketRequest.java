package com.tickettriage.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TicketRequest {

    @NotBlank(message = "title is required")
    @Size(max = 255, message = "title must be 255 characters or fewer")
    private String title;

    @Size(max = 2000, message = "description must be 2000 characters or fewer")
    private String description;

    public TicketRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
