package com.womakerscode.meetup.controller;

import com.womakerscode.meetup.model.dto.RegistrationOnMeetupDTO;
import com.womakerscode.meetup.model.entity.Meetup;
import com.womakerscode.meetup.model.entity.Registration;
import com.womakerscode.meetup.service.MeetupRegistrationService;
import com.womakerscode.meetup.service.MeetupService;
import com.womakerscode.meetup.service.RegistrationService;
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

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = {MeetupRegistrationController.class})
@AutoConfigureMockMvc
class MeetupRegistrationControllerTest {

    private static String MEETUP_REGISTRATION_API = "/api/meetup/registration/connect/";
    private static Integer MEETUP_ID = 1;
    private static Integer REGISTRATION_ID = 101;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MeetupRegistrationService service;

    @MockBean
    MeetupService meetupService;

    @MockBean
    RegistrationService registrationService;

    @Test
    @DisplayName("Should create a meetupRegistration with success")
    public void createMeetupRegistration() throws Exception{

        Meetup meetup = createNewMeetup();
        Registration registration = createNewRegistration();
        RegistrationOnMeetupDTO registrationOnMeetupDTO = createNewRegistrationOnMeetupDTO();
        List<Registration> registrations = Arrays.asList(registration);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(registrationOnMeetupDTO);

        BDDMockito.given(meetupService.getMeetupById(registrationOnMeetupDTO.getMeetupId())).willReturn(meetup);
        BDDMockito.given(registrationService.findAllRegistrationsByIds(registrationOnMeetupDTO.getRegistrationsId()))
                .willReturn(registrations);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(MEETUP_REGISTRATION_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("meetupDTO.id").value(MEETUP_ID))
                .andExpect(jsonPath("registrationsDTO", hasSize(1)))
                .andExpect(jsonPath("registrationsDTO[0].id").value(REGISTRATION_ID));
    }

    private RegistrationOnMeetupDTO createNewRegistrationOnMeetupDTO(){
        return RegistrationOnMeetupDTO.builder()
                .meetupId(MEETUP_ID)
                .registrationsId(Arrays.asList(REGISTRATION_ID))
                .build();
    }

    private Meetup createNewMeetup(){
        return Meetup.builder().id(MEETUP_ID).build();
    }

    private Registration createNewRegistration(){
        return Registration.builder().id(REGISTRATION_ID).build();
    }
}