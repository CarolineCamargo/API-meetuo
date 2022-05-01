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

import javax.validation.Valid;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping ("/api/meetup/")
@RequiredArgsConstructor
public class MeetupController {

    private final MeetupService meetupService;
    private final ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MeetupDTO create(@RequestBody @Valid MeetupDTO dto){

        Meetup entity = modelMapper.map(dto, Meetup.class);
        entity.active();
        entity = meetupService.save(entity);

        return modelMapper.map(entity, MeetupDTO.class);
    }

    @PutMapping
    public MeetupDTO update(@RequestBody @Valid MeetupDTO dto){

        Meetup meetup = meetupService.getMeetupById(dto.getId());
        meetup.setName(dto.getName());
        meetup.setDate(dto.getDate());
        meetup.setActivated(dto.isActivated());

        meetupService.save(meetup);

        return modelMapper.map(meetup, MeetupDTO.class);
    }

    @GetMapping
    public Page<MeetupDTO> find(MeetupFilterDTO filterDTO, Pageable pageRequest){

        Page<Meetup> result = meetupService.find(filterDTO, pageRequest);

        List<MeetupDTO> meetups = result
                .getContent()
                .stream()
                .map(entity -> {
                    MeetupDTO meetupDTO = modelMapper.map(entity, MeetupDTO.class);
                    return meetupDTO;
                }).collect(Collectors.toList());
        return new PageImpl<MeetupDTO>(meetups, pageRequest, result.getTotalElements());
    }
}
