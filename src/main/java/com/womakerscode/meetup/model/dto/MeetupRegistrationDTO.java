package com.womakerscode.meetup.model.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeetupRegistrationDTO {

    private MeetupDTO meetupDTO;

    private List<RegistrationDTO> registrationsDTO;
}
