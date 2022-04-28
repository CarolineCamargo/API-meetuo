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

    // precisa validar se meetup já existe? se sim, usar qual atributo já que não tem um único fora o id
    @Override
    public Meetup save(Meetup meetup){
        return repository.save(meetup);
    }

    //Ana usou variavel "loan" por que? essa validação está certa? é necessária?
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
        return repository.findByRegistrationOnMeetup(meetupFilterDTO.getCpf(), meetupFilterDTO.getMeetupName(), pageable);
    }
    //ver comentário na interface
    @Override
    public Page<Meetup> getRegistrationsByMeetup(List<Registration> registration, Pageable pageable){
        return repository.findByRegistration((Registration) registration, pageable);
    }
}
