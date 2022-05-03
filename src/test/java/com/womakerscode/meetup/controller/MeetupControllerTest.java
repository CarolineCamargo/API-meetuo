package com.womakerscode.meetup.controller;

import com.womakerscode.meetup.model.dto.MeetupDTO;
import com.womakerscode.meetup.model.entity.Meetup;
import com.womakerscode.meetup.service.MeetupService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = {MeetupController.class})
@AutoConfigureMockMvc
public class MeetupControllerTest {

    static String MEETUP_API = "/api/meetup/";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MeetupService meetupService;

    @Test
    @DisplayName("Should create a meetup with success")
    public void createMeetup() throws Exception {

        MeetupDTO dto = createNewMeetupDto();

        BDDMockito.given(meetupService.save(any(Meetup.class))).willReturn(createNewMeetup());

        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(MEETUP_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(dto.getId()))
                .andExpect(jsonPath("name").value(dto.getName()))
                .andExpect(jsonPath("date").value(dto.getDate()))
                .andExpect(jsonPath("activated").value(true));
    }

    @Test
    @DisplayName("Should return message when name is empty")
    public void meetupCreateNameEmpty() throws Exception {

        MeetupDTO dto = createNewMeetupDto();
        dto.setName("");

        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(MEETUP_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors").value("O campo nome não pode ser vazio"));

    }

    @Test
    @DisplayName("Should return message when date is empty")
    public void meetupCreateDateEmpty() throws Exception {

        MeetupDTO dto = createNewMeetupDto();
        dto.setDate("");

        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(MEETUP_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors").value("O campo data não pode ser vazio"));
    }

    @Test
    @DisplayName("Should update a meetup")
    public void updateMeetup() throws Exception{

        MeetupDTO dto = createUpdatingMeetupDto();
        Meetup updateMeetup = createNewMeetup();

        String json = new ObjectMapper().writeValueAsString(dto);

        BDDMockito.given(meetupService.getMeetupById(anyInt())).willReturn(updateMeetup);
        BDDMockito.given(meetupService.save(updateMeetup)).willReturn(updateMeetup);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put(MEETUP_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(dto.getId()))
                .andExpect(jsonPath("name").value(dto.getName()))
                .andExpect(jsonPath("date").value(dto.getDate()))
                .andExpect(jsonPath("activated").value(dto.isActivated()));
    }

    @Test
    @DisplayName("Should return message when name is empty")
    public void meetupUpdateNameEmpty() throws Exception {

        MeetupDTO dto = createUpdatingMeetupDto();
        dto.setName("");

        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(MEETUP_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors").value("O campo nome não pode ser vazio"));
    }

    @Test
    @DisplayName("Should return message when date is empty")
    public void meetupUpdateDateEmpty() throws Exception {

        MeetupDTO dto = createUpdatingMeetupDto();
        dto.setDate("");

        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(MEETUP_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors").value("O campo data não pode ser vazio"));
    }

    @Test
    @DisplayName("Should filter meetup")
    public void findMeetup() throws Exception{

        Meetup meetup = createNewMeetup();

        BDDMockito.given(meetupService.find(any(Meetup.class), any(Pageable.class)))
                .willReturn(new PageImpl<Meetup>(List.of(meetup), PageRequest.of(0, 100), 1));

        String queryString = String.format("?name=%s&activated=%s&page=0&size=100",
                meetup.getName(), meetup.isActivated());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(MEETUP_API.concat(queryString))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("content", hasSize(1)))
                .andExpect(jsonPath("totalElements").value(1))
                .andExpect(jsonPath("pageable.pageSize").value(100))
                .andExpect(jsonPath("pageable.pageNumber").value(0));
    }

    public MeetupDTO createNewMeetupDto(){
        return MeetupDTO.builder()
                .id(1)
                .name("WoMakersCode Java")
                .date("01/06/2022")
                .build();
    }

    public MeetupDTO createUpdatingMeetupDto(){
        return MeetupDTO.builder()
                .id(1)
                .name("WoMakersCode")
                .date("20/06/2022")
                .activated(false)
                .build();
    }

    public Meetup createNewMeetup(){
        return Meetup.builder()
                .id(1)
                .name("WoMakersCode Java")
                .date("01/06/2022")
                .activated(true)
                .build();
    }
}
