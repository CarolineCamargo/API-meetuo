package com.womakerscode.meetup.model.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeetupDTO {

    private Integer id;

    private String cpf;

    private String meetupName;

    private RegistrationDTO registrationDTO;
}
