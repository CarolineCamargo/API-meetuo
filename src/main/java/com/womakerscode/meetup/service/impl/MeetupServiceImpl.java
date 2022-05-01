package com.womakerscode.meetup.service.impl;

import com.womakerscode.meetup.controller.exceptions.BusinessException;
import com.womakerscode.meetup.model.entity.Meetup;
import com.womakerscode.meetup.repository.MeetupRepository;
import com.womakerscode.meetup.service.MeetupService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
        return repository.findById(id).orElseThrow(() -> new BusinessException("Id doesn't exist"));
    }

    @Override
    public Page<Meetup> find(Meetup filter, Pageable pageable){

        Example<Meetup> example = Example.of(filter,
                ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withIncludeNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));

        return repository.findAll(example, pageable);
    }
}
