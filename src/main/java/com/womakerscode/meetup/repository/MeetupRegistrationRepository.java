package com.womakerscode.meetup.repository;

import com.womakerscode.meetup.model.entity.MeetupRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetupRegistrationRepository extends JpaRepository <MeetupRegistration, Integer> {
}
