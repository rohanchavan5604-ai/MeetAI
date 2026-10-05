package com.meetai.controller;

import com.meetai.dto.MeetingRequestDTO;
import com.meetai.dto.MeetingResponseDTO;
import com.meetai.service.MeetingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meetings")
public class MeetingController {

    @Autowired
    private MeetingService meetingService;

    @PostMapping
    public ResponseEntity<MeetingResponseDTO> createMeeting(
            @Valid @RequestBody MeetingRequestDTO meetingRequest) {

        MeetingResponseDTO response =
                meetingService.createMeeting(meetingRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeetingResponseDTO> getMeetingById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                meetingService.getMeetingById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<MeetingResponseDTO>> getAllMeetings() {

        return ResponseEntity.ok(
                meetingService.getAllMeetings()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<MeetingResponseDTO> updateMeeting(
            @PathVariable Long id,
            @Valid @RequestBody MeetingRequestDTO meetingRequest) {

        return ResponseEntity.ok(
                meetingService.updateMeeting(
                        id,
                        meetingRequest
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeeting(
            @PathVariable Long id) {

        meetingService.deleteMeeting(id);

        return ResponseEntity.noContent().build();
    }
}