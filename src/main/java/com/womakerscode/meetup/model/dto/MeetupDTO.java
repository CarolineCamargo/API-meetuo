package com.womakerscode.meetup.model.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeetupDTO {

    private Integer id;

    @NotBlank(message = "O campo nome não pode ser vazio")
    private String name;

    @NotBlank(message = "O campo data não pode ser vazio")
    private String date;

    private boolean activated;
}
