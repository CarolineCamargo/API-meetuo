package com.womakerscode.meetup.service;

import com.womakerscode.meetup.controller.exceptions.BusinessException;
import com.womakerscode.meetup.model.entity.Meetup;
import com.womakerscode.meetup.repository.MeetupRepository;
import com.womakerscode.meetup.service.impl.MeetupServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

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

    @Test
    @DisplayName("Should get meetup by id")
    public void getMeetupById(){

        Meetup meetup = createNewMeetup();
        Mockito.when(repository.findById(meetup.getId())).thenReturn(Optional.of(meetup));

        Meetup foundMeetup = meetupService.getMeetupById(meetup.getId());

        assertThat(foundMeetup.getId()).isEqualTo(meetup.getId());
        assertThat(foundMeetup.getName()).isEqualTo(meetup.getName());
        assertThat(foundMeetup.getDate()).isEqualTo(meetup.getDate());
        assertThat(foundMeetup.isActivated()).isEqualTo(meetup.isActivated());
    }

    @Test
    @DisplayName("Should throw exception when meetup not found by id")
    public void getMeetupNotFoundById(){

        Integer id = 2;

        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        Throwable exception = Assertions.catchThrowable(() -> meetupService.getMeetupById(id));
        assertThat(exception).isInstanceOf(BusinessException.class).hasMessage("Id doesn't exist");
    }

    @Test
    @DisplayName("Should filter registration must by properties")
    public void findMeetup(){

        Meetup meetup = createNewMeetup();
        Pageable pageable = PageRequest.of(0, 10);
        List<Meetup> meetupList = List.of(meetup);
        Page<Meetup> page = new PageImpl<>(meetupList, PageRequest.of(0, 10), 1);

        Mockito.when(repository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class))).thenReturn(page);
        Page<Meetup> result = meetupService.find(meetup, pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(meetupList);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }

    public Meetup createNewMeetup(){
        return Meetup.builder()
                .id(1)
                .name("WoMakersCode Java")
                .date("01/06/2022")
                .build();
    }
}