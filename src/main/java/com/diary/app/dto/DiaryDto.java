package com.diary.app.dto;

import com.diary.app.entity.DiaryEntry.Mood;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class DiaryDto {

    public record CreateEntryRequest(
        @NotBlank String title,
        @NotBlank String content,
        @NotNull Mood mood
    ) {}

    public record UpdateEntryRequest(
        @NotBlank String title,
        @NotBlank String content,
        @NotNull Mood mood
    ) {}

    public record EntryResponse(
        Long id,
        String title,
        String content,
        Mood mood,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime createdAt,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime updatedAt
    ) {}

    public record SearchRequest(
        String keyword,
        Mood mood,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime from,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime to
    ) {}
}
