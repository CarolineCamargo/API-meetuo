package com.womakerscode.meetup.controller;

import com.womakerscode.meetup.controller.exceptions.BusinessException;
import com.womakerscode.meetup.model.dto.*;
import com.womakerscode.meetup.model.entity.Meetup;
import com.womakerscode.meetup.model.entity.MeetupRegistration;
import com.womakerscode.meetup.model.entity.Registration;
import com.womakerscode.meetup.service.MeetupRegistrationService;
import com.womakerscode.meetup.service.MeetupService;
import com.womakerscode.meetup.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping ("/api/meetup/registration/connect/")
public class MeetupRegistrationController {

    private final MeetupRegistrationService meetupRegistrationService;
    private final MeetupService meetupService;
    private final RegistrationService registrationService;
    private final ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MeetupRegistrationDTO create(@RequestBody @Valid RegistrationOnMeetupDTO dto){

        Meetup meetup = meetupService.getMeetupById(dto.getMeetupId());
        List<Registration> registrations = registrationService.findAllRegistrationsByIds(dto.getRegistrationsId());

        List<MeetupRegistration> entities = registrations.stream()
                .map(registration -> MeetupRegistration.builder()
                        .meetup(meetup)
                        .registration(registration)
                        .build())
                .collect(Collectors.toList());

        meetupRegistrationService.saveAll(entities);

        List<RegistrationDTO> registrationDTOs = registrations.stream()
                .map(registration -> modelMapper.map(registration, RegistrationDTO.class))
                .collect(Collectors.toList());

        return MeetupRegistrationDTO.builder()
                .meetupDTO(modelMapper.map(meetup, MeetupDTO.class))
                .registrationsDTO(registrationDTOs)
                .build();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody @Valid RegistrationMeetupDTO dto){

        Meetup meetup = meetupService.getMeetupById(dto.getMeetupId());
        Registration registration = registrationService.getRegistrationById(dto.getRegistrationId())
                .orElseThrow(() -> new BusinessException("Registration not found"));

        List<MeetupRegistration> meetupRegistrationList = meetupRegistrationService.findByMeetupAndRegistration(meetup, registration);

        meetupRegistrationService.deleteAll(meetupRegistrationList);
    }

}
