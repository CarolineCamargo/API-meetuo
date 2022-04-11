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
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
        Registration registration = createValidRegistration();

        Mockito.when(repository.existByRegistration(Mockito.anyString())).thenReturn(false);
        Mockito.when(repository.save(registration)).thenReturn(createValidRegistration());
        Registration savedRegistration = registrationService.save(registration);

        assertThat(savedRegistration.getId()).isEqualTo(101);
        assertThat(savedRegistration.getName()).isEqualTo("Caroline");
        assertThat(savedRegistration.getDateOfRegistration()).isEqualTo("01/04/2022");
        assertThat(savedRegistration.getVersion()).isEqualTo("001");
    }

    @Test
    @DisplayName("Should throw business error when thy to save a new registration duplicated")
    public void shouldNotSaveAsRegistrationDuplicated(){

        Registration registration = createValidRegistration();
        Mockito.when(repository.existByRegistration(Mockito.any())).thenReturn(true);

        Throwable exception = Assertions.catchThrowable(()-> registrationService.save(registration));
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("Registration already created");

        Mockito.verify(repository, Mockito.never()).save(registration);
    }

    @Test
    @DisplayName("Should get a registration by id")
    public void getByRegistrationIdTest(){

        Integer id = 11;
        Registration registration = createValidRegistration();
        registration.setId(id);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(registration));

        Optional<Registration> foundRegistration = registrationService.getRegistrationById(id);

        assertThat(foundRegistration.isPresent()).isTrue();
        assertThat(foundRegistration.get().getId()).isEqualTo(id);
        assertThat(foundRegistration.get().getName()).isEqualTo(registration.getName());
        assertThat(foundRegistration.get().getDateOfRegistration()).isEqualTo(registration.getDateOfRegistration());
        assertThat(foundRegistration.get().getVersion()).isEqualTo(registration.getVersion());
    }

    @Test
    @DisplayName("Should return empty when get a registration by id when doesn't exists")
    public void registrationNotFoundByIdTest(){

        Integer id = 11;
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        Optional <Registration> registration = registrationService.getRegistrationById(id);

        assertThat(registration.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Should delete a student")
    public void deleteRegistrationTest(){

        Registration registration = Registration.builder().id(11).build();

        assertDoesNotThrow(() -> registrationService.delete(registration));

        Mockito.verify(repository, Mockito.times(1)).delete(registration);
    }

    @Test
    @DisplayName("Should update a registration")
    public void updateRegistration(){

        Integer id = 11;
        Registration updatingRegistration = Registration.builder().id(11).build();

        Registration updatedRegistration = createValidRegistration();
        updatedRegistration.setId(id);
        Mockito.when(repository.save(updatingRegistration)).thenReturn(updatedRegistration);
        Registration registration = registrationService.update(updatingRegistration);

        assertThat(registration.getId()).isEqualTo(updatedRegistration.getId());
        assertThat(registration.getName()).isEqualTo(updatedRegistration.getName());
        assertThat(registration.getDateOfRegistration()).isEqualTo(updatedRegistration.getDateOfRegistration());
        assertThat(registration.getVersion()).isEqualTo(updatedRegistration.getVersion());
    }

    @Test
    @DisplayName("Should filter registration must by properties")
    public void findRegistrationTest(){

        Registration registration = createValidRegistration();
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Registration> listRegistration = Arrays.asList(registration);
        Page<Registration> page =
                new PageImpl<Registration>(listRegistration, PageRequest.of(0, 10),1);

        Mockito.when(repository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class))).thenReturn(page);
        Page<Registration> result = registrationService.find(registration, pageRequest);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(listRegistration);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("Should get a registration by version")
    public void getRegistrationByVersion(){

        String version = "010";

        Mockito.when(repository.findByVersion(version))
                .thenReturn(Optional.of(Registration.builder().id(11).version(version).build()));
        Optional<Registration> registration = registrationService.getRegistrationByVersion(version);

        assertThat(registration.isPresent()).isTrue();
        assertThat(registration.get().getId()).isEqualTo(11);
        assertThat(registration.get().getVersion()).isEqualTo(version);
        Mockito.verify(repository, Mockito.times(1)).findByVersion(version);
    }

    private Registration createValidRegistration() {
        return Registration.builder()
                .id(101)
                .name("Caroline")
                .dateOfRegistration("01/04/2022")
                .version("001")
                .build();
    }
}
