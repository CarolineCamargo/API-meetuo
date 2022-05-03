package com.womakerscode.meetup.controller;

import com.womakerscode.meetup.controller.exceptions.BusinessException;
import com.womakerscode.meetup.model.dto.*;
import com.womakerscode.meetup.model.entity.Meetup;
import com.womakerscode.meetup.model.entity.MeetupRegistration;
import com.womakerscode.meetup.model.entity.Registration;
import com.womakerscode.meetup.service.MeetupRegistrationService;
import com.womakerscode.meetup.service.MeetupService;
import com.womakerscode.meetup.service.RegistrationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
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

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = {MeetupRegistrationController.class})
@AutoConfigureMockMvc
class MeetupRegistrationControllerTest {

    private static final String MEETUP_REGISTRATION_API = "/api/meetup/registration/connect";
    private static final Integer MEETUP_ID = 1;
    private static final Integer REGISTRATION_ID = 101;

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

        RegistrationOnMeetupDTO registrationOnMeetupDTO = createNewRegistrationOnMeetupDTO();
        List<Registration> registrations = List.of(createNewRegistration());

        String json = new ObjectMapper().writeValueAsString(registrationOnMeetupDTO);

        BDDMockito.given(meetupService.getMeetupById(registrationOnMeetupDTO.getMeetupId())).willReturn(createNewMeetup());
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

    @Test
    @DisplayName("Should delete a meetupRegistration")
    public void deleteMeetupRegistration() throws Exception{

        Meetup meetup = createNewMeetup();
        Registration registration = createNewRegistration();
        List<MeetupRegistration> meetupRegistrationList = createNewMeetupRegistration();

        BDDMockito.given(meetupService.getMeetupById(MEETUP_ID)).willReturn(meetup);
        BDDMockito.given(registrationService.getRegistrationById(REGISTRATION_ID))
                .willReturn(Optional.of(registration));
        BDDMockito.given(service.findByMeetupAndRegistration(meetup, registration))
                .willReturn(meetupRegistrationList);

        String json = new ObjectMapper().writeValueAsString(createNewRegistrationMeetupDTO());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(MEETUP_REGISTRATION_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder).andExpect(status().isNoContent());

        Mockito.verify(service, Mockito.times(1)).deleteAll(meetupRegistrationList);
    }
    @Test
    @DisplayName("Should return message when delete a meetupRegistration and registration not found")
    public void deleteMeetupRegistrationWhenRegistrationNotFound() throws Exception{

        BDDMockito.given(registrationService.getRegistrationById(REGISTRATION_ID))
                .willThrow(new BusinessException("Registration not found"));

        String json = new ObjectMapper().writeValueAsString(createNewRegistrationMeetupDTO());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete(MEETUP_REGISTRATION_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors").value("Registration not found"));
    }

    @Test
    @DisplayName("Should return message when meetupId is null")
    public void meetupRegistrationMeetupIdNotNull() throws Exception {

        RegistrationMeetupDTO dto = RegistrationMeetupDTO.builder().registrationId(REGISTRATION_ID).build();

        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(MEETUP_REGISTRATION_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors").value("O campo meetupId não pode ser nulo"));
    }

    @Test
    @DisplayName("Should return message when registrationId is null")
    public void meetupRegistrationRegistrationIdNotNull() throws Exception {

        RegistrationMeetupDTO dto = RegistrationMeetupDTO.builder().meetupId(MEETUP_ID).build();

        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(MEETUP_REGISTRATION_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors").value("O campo registrationId não pode ser nulo"));
    }

    @Test
    @DisplayName("Should find meetupRegistration by meetup")
    public void findByMeetup () throws Exception {

        Meetup meetup = createNewMeetup();
        List<MeetupRegistration> meetupRegistrations = createNewMeetupRegistration();

        BDDMockito.given(meetupService.getMeetupById(MEETUP_ID)).willReturn(meetup);
        BDDMockito.given(service.findByMeetup(meetup)).willReturn(meetupRegistrations);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(MEETUP_REGISTRATION_API.concat("/" + MEETUP_ID))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("meetupDTO").value(meetup))
                .andExpect(jsonPath("registrationsDTO", hasSize(1)))
                .andExpect(jsonPath("registrationsDTO[0]").value(createNewRegistrationDTO()));
    }

    private Meetup createNewMeetup(){
        return Meetup.builder()
                .id(MEETUP_ID)
                .name("WoMakersCode Java")
                .date("01/06/2022")
                .activated(true)
                .build();
    }

    private Registration createNewRegistration(){
        return Registration.builder()
                .id(REGISTRATION_ID)
                .name("Caroline")
                .dateOfRegistration("01/04/2022")
                .cpf("012345678900")
                .build();
    }

    private RegistrationMeetupDTO createNewRegistrationMeetupDTO() {
        return RegistrationMeetupDTO.builder()
                .meetupId(MEETUP_ID)
                .registrationId(REGISTRATION_ID)
                .build();
    }

    private RegistrationOnMeetupDTO createNewRegistrationOnMeetupDTO(){
        return RegistrationOnMeetupDTO.builder()
                .meetupId(MEETUP_ID)
                .registrationsId(List.of(REGISTRATION_ID))
                .build();
    }

    private List<MeetupRegistration> createNewMeetupRegistration() {
         return List.of(MeetupRegistration.builder()
                 .meetup(createNewMeetup())
                 .registration(createNewRegistration())
                 .build());
    }


    private RegistrationDTO createNewRegistrationDTO() {
        return RegistrationDTO.builder()
                .id(REGISTRATION_ID)
                .name("Caroline")
                .dateOfRegistration("01/04/2022")
                .cpf("012345678900")
                .build();
    }
}