package com.womakerscode.meetup.service.impl;

import com.womakerscode.meetup.controller.exceptions.BusinessException;
import com.womakerscode.meetup.model.entity.Meetup;
import com.womakerscode.meetup.model.entity.MeetupRegistration;
import com.womakerscode.meetup.model.entity.Registration;
import com.womakerscode.meetup.repository.MeetupRegistrationRepository;
import com.womakerscode.meetup.service.MeetupRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetupRegistrationServiceImpl implements MeetupRegistrationService {

    private final MeetupRegistrationRepository repository;

    @Override
    public List<MeetupRegistration> saveAll(List<MeetupRegistration> meetupRegistrations){
        return repository.saveAll(meetupRegistrations);
    }

    @Override
    public List<MeetupRegistration> findByMeetupAndRegistration(Meetup meetup, Registration registration) {
        return repository.findByMeetupAndRegistration(meetup, registration);
    }

    @Override
    public void deleteAll(List<MeetupRegistration> meetupRegistrationList) {
        if(CollectionUtils.isEmpty(meetupRegistrationList)){
            throw new BusinessException("meetupRegistration already was deleted");
        }
        repository.deleteAll(meetupRegistrationList);
    }

    @Override
    public List<MeetupRegistration> findByMeetup(Meetup meetup){
        return repository.findByMeetup(meetup);
    }
}
