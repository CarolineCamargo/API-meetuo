package com.womakerscode.meetup.controller;

import com.womakerscode.meetup.model.dto.MeetupDTO;
import com.womakerscode.meetup.model.entity.Meetup;
import com.womakerscode.meetup.service.MeetupService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public Page<MeetupDTO> find(MeetupDTO dto, Pageable pageable){

        Meetup filter = modelMapper.map(dto, Meetup.class);
        Page<Meetup> result = meetupService.find(filter, pageable);

        List<MeetupDTO> dtoList = result
                .getContent()
                .stream()
                .map(entity -> modelMapper.map(entity, MeetupDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<MeetupDTO>(dtoList, pageable, result.getTotalElements());
    }
}
