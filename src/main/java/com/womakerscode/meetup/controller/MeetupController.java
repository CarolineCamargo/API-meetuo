package com.womakerscode.meetup.controller;

import com.womakerscode.meetup.model.dto.MeetupDTO;
import com.womakerscode.meetup.model.dto.MeetupFilterDTO;
import com.womakerscode.meetup.model.dto.RegistrationDTO;
import com.womakerscode.meetup.model.entity.Meetup;
import com.womakerscode.meetup.model.entity.Registration;
import com.womakerscode.meetup.service.MeetupService;
import com.womakerscode.meetup.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping ("/api/meetup")
@RequiredArgsConstructor //por que aqui tem e no registration não?
public class MeetupController {

    private final MeetupService meetupService;
    private final RegistrationService registrationService;
    private final ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private Integer create(@RequestBody MeetupDTO meetupDTO){

        Registration registration = registrationService.getRegistrationByCpf(meetupDTO.getCpf())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        Meetup entity = Meetup.builder()
                .registration((List<Registration>) registration) //isso ta certo??
                .meetupName("WoMakersCode Java")
                .meetupDate("01/06/2022")
                .build();

        entity = meetupService.save(entity);
        return entity.getId();
    }

    @GetMapping
    public Page<MeetupDTO> find(MeetupFilterDTO filterDTO, Pageable pageRequest){

        Page<Meetup> result = meetupService.find(filterDTO, pageRequest);

        List<MeetupDTO> meetups = result
                .getContent()
                .stream()
                .map(entity -> {
                    Registration registration = (Registration) entity.getRegistration(); //isso vai funcionar?
                    RegistrationDTO registrationDTO = modelMapper.map(registration, RegistrationDTO.class);

                    MeetupDTO meetupDTO = modelMapper.map(entity, MeetupDTO.class);
                    meetupDTO.setRegistrationDTO(registrationDTO);
                    return meetupDTO;
                }).collect(Collectors.toList());
        return new PageImpl<MeetupDTO>(meetups, pageRequest, result.getTotalElements());
    }
}
