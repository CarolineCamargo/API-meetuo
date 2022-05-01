package com.womakerscode.meetup.service.impl;

import com.womakerscode.meetup.model.dto.MeetupFilterDTO;
import com.womakerscode.meetup.model.entity.Meetup;
import com.womakerscode.meetup.model.entity.Registration;
import com.womakerscode.meetup.repository.MeetupRepository;
import com.womakerscode.meetup.service.MeetupService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public Meetup update(Meetup meetup){
        if (meetup == null || meetup.getId() == null){
            throw new IllegalArgumentException("Id cannot be null");
        }
        return repository.save(meetup);
    }
    // precisa validar se o id existe? esse método não retorna exception, tem algo a ver?
    // coloquei a validação no registration e dois testes não passaram, é por que não precisa validar?
    @Override
    public Optional<Meetup> getMeetupById(Integer id){
        return repository.findById(id);
    }

    @Override
    public Page<Meetup> find(MeetupFilterDTO meetupFilterDTO, Pageable pageable){
        return null; //repository.findByCpfOnMeetup(meetupFilterDTO.getCpf(), meetupFilterDTO.getMeetupName(), pageable);
    }

    @Override
    public Page<Meetup> getRegistrationsByMeetup(List<Registration> registration, Pageable pageable){
        return null;//repository.findByRegistration((Registration) registration, pageable);
    }
}
