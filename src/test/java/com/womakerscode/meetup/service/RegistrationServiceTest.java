package com.womakerscode.meetup.service;

import com.womakerscode.meetup.exception.BusinessException;
import com.womakerscode.meetup.model.entity.Registration;
import com.womakerscode.meetup.repository.RegistrationRepository;
import com.womakerscode.meetup.service.impl.RegistrationServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@ActiveProfiles ("test")
@ExtendWith(SpringExtension.class)
public class RegistrationServiceTest {

    RegistrationService registrationService;

    @MockBean
    private RegistrationRepository repository;

    @BeforeEach
    public void setUp (){
        this.registrationService = new RegistrationServiceImpl(repository);
    }

    @Test
    @DisplayName("Should save a registration")
    public void saveRegistration(){
        Registration registration = createNewRegistration();

        Mockito.when(repository.existsByCpf(Mockito.anyString())).thenReturn(false);
        Mockito.when(repository.save(registration)).thenReturn(createNewRegistration());
        Registration savedRegistration = registrationService.save(registration);

        assertThat(savedRegistration.getId()).isEqualTo(101);
        assertThat(savedRegistration.getName()).isEqualTo("Caroline");
        assertThat(savedRegistration.getDateOfRegistration()).isEqualTo("01/04/2022");
        assertThat(savedRegistration.getCpf()).isEqualTo("12345678900");
    }

    @Test
    @DisplayName("Should throw business error when thy to save a new registration duplicated")
    public void shouldNotSaveAsRegistrationDuplicated(){

        Registration registration = createNewRegistration();
        Mockito.when(repository.existsByCpf(Mockito.any())).thenReturn(true);

        Throwable exception = Assertions.catchThrowable(()-> registrationService.save(registration));
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Registration already created");

        Mockito.verify(repository, Mockito.never()).save(registration);
    }

    @Test
    @DisplayName("Should get a registration by id")
    public void getRegistrationById(){

        Integer id = 11;
        Registration registration = createNewRegistration();
        registration.setId(id);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(registration));

        Optional<Registration> foundRegistration = registrationService.getRegistrationById(id);

        assertThat(foundRegistration.isPresent()).isTrue();
        assertThat(foundRegistration.get().getId()).isEqualTo(id);
        assertThat(foundRegistration.get().getName()).isEqualTo(registration.getName());
        assertThat(foundRegistration.get().getDateOfRegistration()).isEqualTo(registration.getDateOfRegistration());
        assertThat(foundRegistration.get().getCpf()).isEqualTo(registration.getCpf());
    }

    @Test
    @DisplayName("Should return empty when get a registration by id and doesn't exists")
    public void registrationNotFoundById(){

        Integer id = 11;
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        Optional <Registration> registration = registrationService.getRegistrationById(id);

        assertThat(registration.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Should delete a registration")
    public void deleteRegistration(){

        Registration registration = Registration.builder().id(11).build();

        assertDoesNotThrow(() -> registrationService.delete(registration));

        Mockito.verify(repository, Mockito.times(1)).delete(registration);
    }

    @Test
    @DisplayName("Should update a registration")
    public void updateRegistration(){

        Integer id = 11;
        Registration updatingRegistration = Registration.builder().id(id).build();

        Registration updatedRegistration = createNewRegistration();
        updatedRegistration.setId(id);
        Mockito.when(repository.save(updatingRegistration)).thenReturn(updatedRegistration);
        Registration registration = registrationService.update(updatingRegistration);

        assertThat(registration.getId()).isEqualTo(updatedRegistration.getId());
        assertThat(registration.getName()).isEqualTo(updatedRegistration.getName());
        assertThat(registration.getDateOfRegistration()).isEqualTo(updatedRegistration.getDateOfRegistration());
        assertThat(registration.getCpf()).isEqualTo(updatedRegistration.getCpf());
    }

    @Test
    @DisplayName("Should filter registration must by properties")
    public void findRegistration(){

        Registration registration = createNewRegistration();
        Pageable pageRequest = PageRequest.of(0, 10);
        List<Registration> listRegistration = Arrays.asList(registration);
        Page<Registration> page = new PageImpl<>(listRegistration, PageRequest.of(0, 10),1);

        Mockito.when(repository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class))).thenReturn(page);
        Page<Registration> result = registrationService.find(registration, pageRequest);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(listRegistration);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("Should get a registration by cpf")
    public void getRegistrationByCpf(){

        String cpf = "12345678900";

        Mockito.when(repository.findByCpf(cpf))
                .thenReturn(Optional.of(Registration.builder().id(11).cpf(cpf).build()));
        Optional<Registration> registration = registrationService.getRegistrationByCpf(cpf);

        assertThat(registration.isPresent()).isTrue();
        assertThat(registration.get().getId()).isEqualTo(11);
        assertThat(registration.get().getCpf()).isEqualTo(cpf);
        Mockito.verify(repository, Mockito.times(1)).findByCpf(cpf);
    }

    private Registration createNewRegistration() {

        return Registration.builder()
                .id(101)
                .name("Caroline")
                .dateOfRegistration("01/04/2022")
                .cpf("12345678900")
                .build();
    }
}
