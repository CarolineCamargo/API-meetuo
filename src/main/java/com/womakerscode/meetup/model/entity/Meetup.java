package com.womakerscode.meetup.model.entity;


import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Meetup {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column (name = "meetup_name")
    private String meetupName;

    @Column (name = "meetup_date")
    private String meetupDate;

    @OneToMany (mappedBy = "meetup")
    private List<Registration> registration;
}
