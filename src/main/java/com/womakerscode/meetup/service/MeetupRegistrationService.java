package com.womakerscode.meetup.service;

import com.womakerscode.meetup.model.entity.MeetupRegistration;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MeetupRegistrationService {
    List<MeetupRegistration> saveAll(List<MeetupRegistration> meetupRegistrations);
}
