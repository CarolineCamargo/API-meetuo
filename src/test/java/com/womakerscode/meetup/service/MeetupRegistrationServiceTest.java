package com.womakerscode.meetup.service;

import com.womakerscode.meetup.model.entity.Meetup;
import com.womakerscode.meetup.model.entity.MeetupRegistration;
import com.womakerscode.meetup.model.entity.Registration;
import com.womakerscode.meetup.repository.MeetupRegistrationRepository;
import com.womakerscode.meetup.service.impl.MeetupRegistrationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class MeetupRegistrationServiceTest {

    MeetupRegistrationService service;

    @MockBean
    MeetupRegistrationRepository repository;

    @BeforeEach
    void setUp() {
        this.service = new MeetupRegistrationServiceImpl(repository);
    }

    @Test
    @DisplayName("Should save a meetupRegistration")
    public void saveMeetupRegistration(){

        MeetupRegistration meetupRegistration = createNewMeetupRegistration();
        List<MeetupRegistration> meetupRegistrationList = Arrays.asList(createNewMeetupRegistration());

        Mockito.when(repository.saveAll(meetupRegistrationList))
                .thenReturn(Arrays.asList(createNewMeetupRegistration()));
        List<MeetupRegistration> savedMeetupRegistration = service.saveAll(meetupRegistrationList);

        savedMeetupRegistration.forEach(smr -> {
            assertThat(smr.getId()).isEqualTo(meetupRegistration.getId());
            assertThat(smr.getMeetup()).isEqualTo(meetupRegistration.getMeetup());
            assertThat(smr.getRegistration()).isEqualTo(meetupRegistration.getRegistration());
        });
    }

    public Meetup createNewMeetup(){
        return Meetup.builder()
                .id(1)
                .name("WoMakersCode Java")
                .date("01/06/2022")
                .build();
    }

    public Registration createNewRegistration(){
        return Registration.builder()
                .id(101)
                .name("Caroline")
                .dateOfRegistration("01/04/2022")
                .cpf("012345678900")
                .build();

    }

    public MeetupRegistration createNewMeetupRegistration(){
        return MeetupRegistration.builder()
                .id(10)
                .meetup(createNewMeetup())
                .registration(createNewRegistration())
                .build();
    }
}
