package com.diary.app.service;

import com.diary.app.dto.DiaryDto.*;
import com.diary.app.entity.DiaryEntry;
import com.diary.app.entity.DiaryEntry.Mood;
import com.diary.app.entity.User;
import com.diary.app.repository.DiaryEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryEntryRepository entryRepository;

    public EntryResponse createEntry(CreateEntryRequest request, User user) {
        DiaryEntry entry = DiaryEntry.builder()
            .title(request.title())
            .content(request.content())
            .mood(request.mood())
            .user(user)
            .build();
        return toResponse(entryRepository.save(entry));
    }

    public List<EntryResponse> getEntries(User user) {
        return entryRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
            .stream().map(this::toResponse).toList();
    }

    public EntryResponse getEntry(Long id, User user) {
        return entryRepository.findByIdAndUserId(id, user.getId())
            .map(this::toResponse)
            .orElseThrow(() -> new RuntimeException("Entry not found"));
    }

    public EntryResponse updateEntry(Long id, UpdateEntryRequest request, User user) {
        DiaryEntry entry = entryRepository.findByIdAndUserId(id, user.getId())
            .orElseThrow(() -> new RuntimeException("Entry not found"));
        entry.setTitle(request.title());
        entry.setContent(request.content());
        entry.setMood(request.mood());
        return toResponse(entryRepository.save(entry));
    }

    public void deleteEntry(Long id, User user) {
        DiaryEntry entry = entryRepository.findByIdAndUserId(id, user.getId())
            .orElseThrow(() -> new RuntimeException("Entry not found"));
        entryRepository.delete(entry);
    }

    public List<EntryResponse> searchEntries(SearchRequest request, User user) {
        return entryRepository.searchEntries(
            user.getId(),
            request.keyword(),
            request.mood(),
            request.from(),
            request.to()
        ).stream().map(this::toResponse).toList();
    }

    private EntryResponse toResponse(DiaryEntry e) {
        return new EntryResponse(
            e.getId(), e.getTitle(), e.getContent(),
            e.getMood(), e.getCreatedAt(), e.getUpdatedAt()
        );
    }
}
