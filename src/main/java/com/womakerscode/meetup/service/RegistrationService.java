package com.womakerscode.meetup.service;

import com.womakerscode.meetup.model.entity.Registration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface RegistrationService {

    Registration save(Registration any);

    Optional<Registration> getRegistrationById(Integer id);

    void delete(Registration registration);

    Registration update(Registration registration);

    Page<Registration> find(Registration filter, Pageable pageRequest);

    Optional<Registration> getRegistrationByCpf(String cpf);
}
