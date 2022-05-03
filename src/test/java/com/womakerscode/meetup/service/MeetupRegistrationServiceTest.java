package com.womakerscode.meetup.service;

import com.womakerscode.meetup.controller.exceptions.BusinessException;
import com.womakerscode.meetup.model.entity.Meetup;
import com.womakerscode.meetup.model.entity.MeetupRegistration;
import com.womakerscode.meetup.model.entity.Registration;
import com.womakerscode.meetup.repository.MeetupRegistrationRepository;
import com.womakerscode.meetup.service.impl.MeetupRegistrationServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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
    public void saveMeetupAllRegistration(){

        MeetupRegistration meetupRegistration = createNewMeetupRegistration();
        List<MeetupRegistration> meetupRegistrationList = Arrays.asList(meetupRegistration);

        Mockito.when(repository.saveAll(meetupRegistrationList))
                .thenReturn(Arrays.asList(meetupRegistration));
        List<MeetupRegistration> savedMeetupRegistration = service.saveAll(meetupRegistrationList);

        savedMeetupRegistration.forEach(smr -> {
            assertThat(smr.getId()).isEqualTo(meetupRegistration.getId());
            assertThat(smr.getMeetup()).isEqualTo(meetupRegistration.getMeetup());
            assertThat(smr.getRegistration()).isEqualTo(meetupRegistration.getRegistration());
        });

        Mockito.verify(repository, Mockito.times(1)).saveAll(savedMeetupRegistration);
    }

    @Test
    @DisplayName("Should delete a meetupRegistration")
    public void deleteAllMeetupRegistration(){

        MeetupRegistration meetupRegistration = createNewMeetupRegistration();
        List<MeetupRegistration> meetupRegistrationList = Arrays.asList(meetupRegistration);

        assertDoesNotThrow(() -> service.deleteAll(meetupRegistrationList));

        Mockito.verify(repository, Mockito.times(1)).deleteAll(meetupRegistrationList);
    }

    @Test
    @DisplayName("Should throw exception when delete a meetupRegistration already was deleted")
    public void deleteAllMeetupRegistrationAlreadyDeleted(){

        Throwable exception = Assertions.catchThrowable(()-> service.deleteAll(Collections.emptyList()));
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("meetupRegistration already was deleted");

        Mockito.verify(repository, Mockito.never()).deleteAll(Collections.emptyList());
    }

    @Test
    @DisplayName("Should find meetupRegistration by meetup and registration")
    public void findByMeetupAndRegistration(){

        Meetup meetup = createNewMeetup();
        Registration registration = createNewRegistration();
        MeetupRegistration meetupRegistration = createNewMeetupRegistration();
        List<MeetupRegistration> meetupRegistrationList = Arrays.asList(meetupRegistration);

        Mockito.when(repository.findByMeetupAndRegistration(meetup, registration)).thenReturn(meetupRegistrationList);
        List<MeetupRegistration> savedMeetupRegistrationList = service.findByMeetupAndRegistration(meetup, registration);

        savedMeetupRegistrationList.forEach(smr -> {
            assertThat(smr.getId()).isEqualTo(10);
            assertThat(smr.getMeetup()).isEqualTo(meetup);
            assertThat(smr.getRegistration()).isEqualTo(registration);
        });
    }

    @Test
    @DisplayName("Should find registrations list by meetup")
    public void findByMeetup(){

        Meetup meetup = createNewMeetup();
        Registration registration = createNewRegistration();
        List<MeetupRegistration> meetupRegistrations = Arrays.asList(createNewMeetupRegistration());

        Mockito.when(repository.findByMeetup(meetup)).thenReturn(meetupRegistrations);

        List<MeetupRegistration> meetupRegistrationList = service.findByMeetup(meetup);

        meetupRegistrationList.forEach(r -> {
                assertThat(r.getId()).isEqualTo(10);
                assertThat(r.getMeetup()).isEqualTo(meetup);
                assertThat(r.getRegistration()).isEqualTo(registration);
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
