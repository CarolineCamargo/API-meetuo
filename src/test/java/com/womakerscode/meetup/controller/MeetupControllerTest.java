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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;


import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = {MeetupController.class})
@AutoConfigureMockMvc
public class MeetupControllerTest {

    static String MEETUP_API = "/api/meetup";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MeetupService meetupService;

    @Test
    @DisplayName("Should create a meetup with success")
    public void createMeetup() throws Exception {

        MeetupDTO dto = createNewMeetupDto();
        Meetup savedMeetup = createNewMeetup();

        BDDMockito.given(meetupService.save(any(Meetup.class))).willReturn(savedMeetup);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(MEETUP_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("name").value("WoMakersCode Java"))
                .andExpect(jsonPath("date").value("01/06/2022"));
    }

    @Test
    @DisplayName("Should return message when empty")
    public void meetupNameEmpty() throws Exception {

        MeetupDTO dto = createNewMeetupDto();
        dto.setName("");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(dto);

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
    @DisplayName("Should return message when empty")
    public void meetupDateEmpty() throws Exception {

        MeetupDTO dto = createNewMeetupDto();
        dto.setDate("");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(MEETUP_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors").value("O campo data não pode ser vazio"));
    }

    public MeetupDTO createNewMeetupDto(){
        return MeetupDTO.builder()
                .id(1)
                .name("WoMakersCode Java")
                .date("01/06/2022")
                .build();
    }

    public Meetup createNewMeetup(){
        return Meetup.builder()
                .id(1)
                .name("WoMakersCode Java")
                .date("01/06/2022")
                .build();
    }
}
