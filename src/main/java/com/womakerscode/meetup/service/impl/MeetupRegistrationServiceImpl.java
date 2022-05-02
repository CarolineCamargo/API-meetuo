package com.womakerscode.meetup.service.impl;

import com.womakerscode.meetup.model.entity.MeetupRegistration;
import com.womakerscode.meetup.repository.MeetupRegistrationRepository;
import com.womakerscode.meetup.service.MeetupRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetupRegistrationServiceImpl implements MeetupRegistrationService {

    private final MeetupRegistrationRepository repository;

    @Override
    public List<MeetupRegistration> saveAll (List<MeetupRegistration> meetupRegistrations){
        return repository.saveAll(meetupRegistrations);
    }
}
