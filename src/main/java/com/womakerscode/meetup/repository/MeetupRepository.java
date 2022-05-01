package com.womakerscode.meetup.repository;

import com.womakerscode.meetup.model.entity.Meetup;
import com.womakerscode.meetup.model.entity.Registration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetupRepository extends JpaRepository<Meetup, Integer> {

}
