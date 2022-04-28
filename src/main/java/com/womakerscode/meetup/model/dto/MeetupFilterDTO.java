package com.womakerscode.meetup.model.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeetupFilterDTO {

    private String cpf;

    private String meetupName;
}
