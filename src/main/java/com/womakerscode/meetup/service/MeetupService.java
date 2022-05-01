package com.womakerscode.meetup.service;

import com.womakerscode.meetup.model.dto.MeetupFilterDTO;
import com.womakerscode.meetup.model.entity.Meetup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MeetupService {

    Meetup save(Meetup meetup);

    Meetup getMeetupById(Integer id);

    Page<Meetup> find(MeetupFilterDTO meetupFilterDTO, Pageable pageable);

}
