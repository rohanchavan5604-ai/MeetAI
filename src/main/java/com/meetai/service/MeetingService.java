package com.meetai.service;

import com.meetai.dto.MeetingRequestDTO;
import com.meetai.dto.MeetingResponseDTO;

import java.util.List;

public interface MeetingService {

    MeetingResponseDTO createMeeting(
            MeetingRequestDTO meetingRequest
    );

    MeetingResponseDTO getMeetingById(
            Long id
    );

    List<MeetingResponseDTO> getAllMeetings();

    MeetingResponseDTO updateMeeting(
            Long id,
            MeetingRequestDTO meetingRequest
    );

    void deleteMeeting(
            Long id
    );
}