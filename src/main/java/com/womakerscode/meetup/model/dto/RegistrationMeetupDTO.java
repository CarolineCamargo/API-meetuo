package com.womakerscode.meetup.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationMeetupDTO {

    @NotNull (message = "O campo meetupId não pode ser nulo")
    private Integer meetupId;

    @NotNull (message = "O campo registrationId não pode ser nulo")
    private Integer registrationId;
}
