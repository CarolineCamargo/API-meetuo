package com.womakerscode.meetup.repository;

import com.womakerscode.meetup.model.entity.Meetup;
import com.womakerscode.meetup.model.entity.MeetupRegistration;
import com.womakerscode.meetup.model.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetupRepository extends JpaRepository<Meetup, Integer> {}
