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

    @Query(value = " select l from Meetup as l join l.registration as b where b.cpf = :cpf or l.meetupName =:meetupName ")
    Page<Meetup> findByRegistrationOnMeetup(
            @Param("cpf") String cpf,
            @Param("meetupName") String meetupName,
            Pageable pageable
    );

    Page<Meetup> findByRegistration(Registration registration, Pageable pageable);

}
