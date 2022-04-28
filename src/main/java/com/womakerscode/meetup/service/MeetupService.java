package com.womakerscode.meetup.service;

import com.womakerscode.meetup.model.dto.MeetupFilterDTO;
import com.womakerscode.meetup.model.entity.Meetup;
import com.womakerscode.meetup.model.entity.Registration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MeetupService {
    Meetup save(Meetup meetup);

    Meetup update(Meetup meetup);

    Optional<Meetup> getMeetupById(Integer id);

    Page<Meetup> find(MeetupFilterDTO meetupFilterDTO, Pageable pageable);

    //se meu atributo é uma lista no model, aqui tem que passar uma lista??
    Page<Meetup> getRegistrationsByMeetup(List<Registration> registration, Pageable pageable);
}
