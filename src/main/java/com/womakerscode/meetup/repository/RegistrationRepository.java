package com.womakerscode.meetup.repository;

import com.womakerscode.meetup.model.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegistrationRepository extends JpaRepository <Registration, Integer> {

    boolean existByCpf(String cpf);

    Optional<Registration> findByCpf(String cpf);
}
