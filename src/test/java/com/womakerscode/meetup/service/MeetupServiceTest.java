package com.womakerscode.meetup.service;

import com.womakerscode.meetup.model.entity.Meetup;
import com.womakerscode.meetup.repository.MeetupRepository;
import com.womakerscode.meetup.service.impl.MeetupServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class MeetupServiceTest {

    MeetupService meetupService;

    @MockBean
    MeetupRepository repository;

    @BeforeEach
    void setUp() {
        this.meetupService = new MeetupServiceImpl(repository);
    }

    @Test
    @DisplayName("Should save a meetup")
    public void saveMeetup() {

        Meetup meetup = createNewMeetup();

        Mockito.when(repository.save(meetup)).thenReturn(createNewMeetup());
        Meetup savedMeetup = meetupService.save(meetup);

        assertThat(savedMeetup.getId()).isEqualTo(1);
        assertThat(savedMeetup.getName()).isEqualTo("WoMakersCode Java");
        assertThat(savedMeetup.getDate()).isEqualTo("01/06/2022");
        Mockito.verify(repository, Mockito.times(1)).save(meetup);
    }

    public Meetup createNewMeetup(){
        return Meetup.builder()
                .id(1)
                .name("WoMakersCode Java")
                .date("01/06/2022")
                .build();
    }
}