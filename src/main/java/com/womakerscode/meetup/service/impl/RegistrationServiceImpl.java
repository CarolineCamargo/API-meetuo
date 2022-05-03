package com.womakerscode.meetup.service.impl;

import com.womakerscode.meetup.controller.exceptions.BusinessException;
import com.womakerscode.meetup.model.entity.Registration;
import com.womakerscode.meetup.repository.RegistrationRepository;
import com.womakerscode.meetup.service.RegistrationService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    RegistrationRepository repository;

    public RegistrationServiceImpl(RegistrationRepository repository) {
        this.repository = repository;
    }

    public Registration save(Registration registration) {
        if (repository.existsByCpf(registration.getCpf())){
            throw new BusinessException("Registration already created");
        }
        return repository.save(registration);
    }

    @Override
    public Optional<Registration> getRegistrationById(Integer id) {
        return this.repository.findById(id);
    }

    @Override
    public void delete(Registration registration) {
        this.repository.delete(registration);
    }

    @Override
    public Registration update(Registration registration) {
        if (registration == null || registration.getId() == null){
            throw new IllegalArgumentException("Id cannot be null");
        }
        return this.repository.save(registration);
    }

    @Override
    public Page<Registration> find(Registration filter, Pageable pageRequest) {
        Example<Registration> example = Example.of(filter,
                ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withIncludeNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));

        return repository.findAll(example, pageRequest);
    }

    @Override
    public Optional<Registration> getRegistrationByCpf(String cpf) {
        return repository.findByCpf(cpf);
    }

    @Override
    public List<Registration> findAllRegistrationsByIds(List<Integer> ids){
        return repository.findAllById(ids);
    }
}
