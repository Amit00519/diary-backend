package com.diary.app.repository;

import com.diary.app.entity.DiaryEntry;
import com.diary.app.entity.DiaryEntry.Mood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DiaryEntryRepository extends JpaRepository<DiaryEntry, Long> {

    List<DiaryEntry> findByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<DiaryEntry> findByIdAndUserId(Long id, Long userId);

    @Query("""
        SELECT d FROM DiaryEntry d
        WHERE d.user.id = :userId
          AND (:keyword IS NULL OR LOWER(d.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(d.content) LIKE LOWER(CONCAT('%', :keyword, '%')))
          AND (:mood IS NULL OR d.mood = :mood)
          AND (:from IS NULL OR d.createdAt >= :from)
          AND (:to IS NULL OR d.createdAt <= :to)
        ORDER BY d.createdAt DESC
    """)
    List<DiaryEntry> searchEntries(
        @Param("userId") Long userId,
        @Param("keyword") String keyword,
        @Param("mood") Mood mood,
        @Param("from") LocalDateTime from,
        @Param("to") LocalDateTime to
    );
}
