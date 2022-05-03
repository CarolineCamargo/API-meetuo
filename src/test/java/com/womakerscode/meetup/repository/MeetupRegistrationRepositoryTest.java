package com.womakerscode.meetup.repository;

import com.womakerscode.meetup.model.entity.Meetup;
import com.womakerscode.meetup.model.entity.MeetupRegistration;
import com.womakerscode.meetup.model.entity.Registration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class MeetupRegistrationRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    MeetupRegistrationRepository repository;

    @Test
    @DisplayName("Should find meetupRegistration by meetup and registration")
    public void findByMeetupAndRegistration(){

        Meetup meetup = entityManager.persist(createNewMeetup());
        Registration registration = entityManager.persist(createNewRegistration());
        MeetupRegistration meetupRegistration = entityManager.persist(createNewMeetupRegistration(meetup, registration));

        List<MeetupRegistration> meetupRegistrationList = repository.findByMeetupAndRegistration(meetup, registration);

        meetupRegistrationList.forEach(mrl -> {
            assertThat(mrl.getId()).isEqualTo(meetupRegistration.getId());
            assertThat(mrl.getMeetup()).isEqualTo(meetupRegistration.getMeetup());
            assertThat(mrl.getRegistration()).isEqualTo(meetupRegistration.getRegistration());
        });
    }

    @Test
    @DisplayName("Should find meetupRegistration by meetup")
    public void findByMeetup(){

        Meetup meetup = entityManager.persist(createNewMeetup());
        Registration registration = entityManager.persist(createNewRegistration());
        MeetupRegistration meetupRegistration = entityManager.persist(createNewMeetupRegistration(meetup, registration));

        List<MeetupRegistration> meetupRegistrationList = repository.findByMeetup(meetup);

        meetupRegistrationList.forEach(mrl -> {
            assertThat(mrl.getId()).isEqualTo(meetupRegistration.getId());
            assertThat(mrl.getMeetup()).isEqualTo(meetupRegistration.getMeetup());
            assertThat(mrl.getRegistration()).isEqualTo(meetupRegistration.getRegistration());
        });

    }

    public Meetup createNewMeetup(){
        return Meetup.builder()
                .name("WoMakersCode Java")
                .date("01/06/2022")
                .activated(true)
                .build();
    }

    public Registration createNewRegistration(){
        return Registration.builder()
                .name("Caroline")
                .dateOfRegistration("01/04/2022")
                .cpf("012345678900")
                .build();
    }

    public MeetupRegistration createNewMeetupRegistration(Meetup meetup, Registration registration){
        return MeetupRegistration.builder()
                .meetup(meetup)
                .registration(registration)
                .build();
    }
}
