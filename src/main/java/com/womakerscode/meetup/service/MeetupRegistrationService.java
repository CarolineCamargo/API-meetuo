package com.womakerscode.meetup.service;

import com.womakerscode.meetup.model.entity.Meetup;
import com.womakerscode.meetup.model.entity.MeetupRegistration;
import com.womakerscode.meetup.model.entity.Registration;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MeetupRegistrationService {
    List<MeetupRegistration> saveAll(List<MeetupRegistration> meetupRegistrations);

    List<MeetupRegistration> findByMeetupAndRegistration(Meetup meetup, Registration registration);

    void deleteAll(List<MeetupRegistration> meetupRegistrationList);
}
