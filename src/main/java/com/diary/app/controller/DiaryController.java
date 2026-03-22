package com.diary.app.controller;

import com.diary.app.dto.DiaryDto.*;
import com.diary.app.entity.User;
import com.diary.app.service.DiaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/entries")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping
    public ResponseEntity<EntryResponse> create(
        @Valid @RequestBody CreateEntryRequest request,
        @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(diaryService.createEntry(request, user));
    }

    @GetMapping
    public ResponseEntity<List<EntryResponse>> getAll(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(diaryService.getEntries(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntryResponse> getOne(
        @PathVariable Long id,
        @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(diaryService.getEntry(id, user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntryResponse> update(
        @PathVariable Long id,
        @Valid @RequestBody UpdateEntryRequest request,
        @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(diaryService.updateEntry(id, request, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
        @PathVariable Long id,
        @AuthenticationPrincipal User user) {
        diaryService.deleteEntry(id, user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/search")
    public ResponseEntity<List<EntryResponse>> search(
        @RequestBody SearchRequest request,
        @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(diaryService.searchEntries(request, user));
    }
}
