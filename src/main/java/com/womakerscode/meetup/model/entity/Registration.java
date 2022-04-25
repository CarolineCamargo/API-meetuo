package com.womakerscode.meetup.model.entity;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table
public class Registration {

    @Id
    @Column
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column (name = "person_name")
    private String name;

    @Column (name = "date_of_registration")
    private String dateOfRegistration;

    @Column
    private String cpf;
}
