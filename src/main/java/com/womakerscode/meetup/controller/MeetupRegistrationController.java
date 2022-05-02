package com.womakerscode.meetup.controller;

import com.womakerscode.meetup.model.dto.MeetupDTO;
import com.womakerscode.meetup.model.dto.MeetupRegistrationDTO;
import com.womakerscode.meetup.model.dto.RegistrationDTO;
import com.womakerscode.meetup.model.dto.RegistrationOnMeetupDTO;
import com.womakerscode.meetup.model.entity.Meetup;
import com.womakerscode.meetup.model.entity.MeetupRegistration;
import com.womakerscode.meetup.model.entity.Registration;
import com.womakerscode.meetup.service.MeetupRegistrationService;
import com.womakerscode.meetup.service.MeetupService;
import com.womakerscode.meetup.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping ("/api/meetup/registration/connect")
public class MeetupRegistrationController {

    private final MeetupRegistrationService meetupRegistrationService;
    private final MeetupService meetupService;
    private final RegistrationService registrationService;
    private final ModelMapper modelMapper;

    @PostMapping
    private MeetupRegistrationDTO create(@RequestBody @Valid RegistrationOnMeetupDTO dto){

        Meetup meetup = meetupService.getMeetupById(dto.getMeetupId());
        List<Registration> registrations = registrationService.findAllRegistrationsByIds(dto.getRegistrationsId());

        List<MeetupRegistration> entities = registrations.stream()
                .map(registration -> MeetupRegistration.builder()
                        .meetup(meetup)
                        .registration(registration)
                        .build())
                .collect(Collectors.toList());

        meetupRegistrationService.saveAll(entities);

        List<RegistrationDTO> registrationDTOs = registrations.stream().map(registration -> modelMapper.map(registration, RegistrationDTO.class)).collect(Collectors.toList());

        return MeetupRegistrationDTO.builder()
                .meetupDTO(modelMapper.map(meetup, MeetupDTO.class))
                .registrations(registrationDTOs)
                .build();
    }

}
