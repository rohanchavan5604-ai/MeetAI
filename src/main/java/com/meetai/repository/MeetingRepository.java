package com.meetai.repository;

import com.meetai.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    List<Meeting> findByCreatedBy(Long userId);

    Optional<Meeting> findByIdAndCreatedBy(Long id, Long userId);
}