package com.womakerscode.meetup.service.impl;

import com.womakerscode.meetup.model.dto.MeetupFilterDTO;
import com.womakerscode.meetup.model.entity.Meetup;
import com.womakerscode.meetup.repository.MeetupRepository;
import com.womakerscode.meetup.service.MeetupService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MeetupServiceImpl implements MeetupService {

    private MeetupRepository repository;

    public MeetupServiceImpl(MeetupRepository repository) {
        this.repository = repository;
    }

    @Override
    public Meetup save(Meetup meetup){
        return repository.save(meetup);
    }

    @Override
    public Meetup getMeetupById(Integer id){
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public Page<Meetup> find(MeetupFilterDTO meetupFilterDTO, Pageable pageable){
        return null; //repository.findByCpfOnMeetup(meetupFilterDTO.getCpf(), meetupFilterDTO.getMeetupName(), pageable);
    }

}
