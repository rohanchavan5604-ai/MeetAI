package com.meetai.service.impl;

import com.meetai.dto.MeetingRequestDTO;
import com.meetai.dto.MeetingResponseDTO;
import com.meetai.entity.Meeting;
import com.meetai.entity.MeetingStatus;
import com.meetai.entity.User;
import com.meetai.exception.MeetingNotFoundException;
import com.meetai.repository.MeetingRepository;
import com.meetai.repository.UserRepository;
import com.meetai.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MeetingServiceImpl implements MeetingService {

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public MeetingResponseDTO createMeeting(MeetingRequestDTO meetingRequest) {

        Meeting meeting = new Meeting();

        meeting.setTitle(meetingRequest.getTitle());
        meeting.setDescription(meetingRequest.getDescription());
        meeting.setStartTime(meetingRequest.getStartTime());
        meeting.setEndTime(meetingRequest.getEndTime());
        meeting.setStatus(MeetingStatus.SCHEDULED);

        LocalDateTime now = LocalDateTime.now();

        meeting.setCreatedAt(now);
        meeting.setUpdatedAt(now);

        User user = getLoggedInUser();

        meeting.setCreatedBy(user.getId());

        Meeting savedMeeting = meetingRepository.save(meeting);

        return convertToResponse(savedMeeting);
    }

    @Override
    public MeetingResponseDTO getMeetingById(Long id) {

        User user = getLoggedInUser();

        Meeting meeting = meetingRepository
                .findByIdAndCreatedBy(id, user.getId())
                .orElseThrow(() ->
                        new MeetingNotFoundException(
                                "Meeting not found with id: " + id
                        )
                );

        return convertToResponse(meeting);
    }

    @Override
    public List<MeetingResponseDTO> getAllMeetings() {

        User user = getLoggedInUser();

        return meetingRepository
                .findByCreatedBy(user.getId())
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public MeetingResponseDTO updateMeeting(
            Long id,
            MeetingRequestDTO meetingRequest) {

        User user = getLoggedInUser();

        Meeting meeting = meetingRepository
                .findByIdAndCreatedBy(id, user.getId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Meeting not found with id: " + id
                        )
                );

        meeting.setTitle(meetingRequest.getTitle());
        meeting.setDescription(meetingRequest.getDescription());
        meeting.setStartTime(meetingRequest.getStartTime());
        meeting.setEndTime(meetingRequest.getEndTime());
        meeting.setUpdatedAt(LocalDateTime.now());

        Meeting updatedMeeting = meetingRepository.save(meeting);

        return convertToResponse(updatedMeeting);
    }

    @Override
    public void deleteMeeting(Long id) {

        User user = getLoggedInUser();

        Meeting meeting = meetingRepository
                .findByIdAndCreatedBy(id, user.getId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Meeting not found with id: " + id
                        )
                );

        meetingRepository.delete(meeting);
    }

    private User getLoggedInUser() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );
    }

    private MeetingResponseDTO convertToResponse(Meeting meeting) {

        return new MeetingResponseDTO(
                meeting.getId(),
                meeting.getTitle(),
                meeting.getDescription(),
                meeting.getStartTime(),
                meeting.getEndTime(),
                meeting.getStatus(),
                meeting.getCreatedAt(),
                meeting.getUpdatedAt(),
                meeting.getCreatedBy()
        );
    }
}