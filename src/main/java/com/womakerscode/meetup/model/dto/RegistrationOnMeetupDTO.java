package com.womakerscode.meetup.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationOnMeetupDTO {

    @NotNull
    private Integer meetupId;

    @NotEmpty
    private List<Integer> registrationsId;
}
