package com.womakerscode.meetup.repository;

import com.womakerscode.meetup.model.entity.Registration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class RegistrationRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    RegistrationRepository repository;

    @Test
    @DisplayName("Should return true when exists a registration already created")
    public void returnTrueWhenRegistrationExists(){

        String cpf = "012345678900";
        Registration registration = createNewRegistration(cpf);
        entityManager.persist(registration);

        boolean exists = repository.existsByCpf(cpf);

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should return false when doesn't exists a cpf with a registration already created")
    public void returnFalseWhenRegistrationDoesntExists(){

        String cpf = "012345678900";

        boolean exists = repository.existsByCpf(cpf);

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Should get a registration by id")
    public void findById(){

        Registration registration = createNewRegistration("012345678900");
        entityManager.persist(registration);

        Optional<Registration> foundRegistration = repository.findById(registration.getId());

        assertThat(foundRegistration.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Should save a registration")
    public void saveRegistration(){

        Registration registration = createNewRegistration("012345678900");

        Registration savedRegistration = repository.save(registration);

        assertThat(savedRegistration.getId()).isNotNull();
    }

    @Test
    @DisplayName("Should delete a registration from database")
    public void deleteRegistration(){

        Registration registration = createNewRegistration("012345678900");
        entityManager.persist(registration);
        Registration foundRegistration = entityManager.find(Registration.class, registration.getId());

        repository.delete(foundRegistration);
        Registration deletedRegistration = entityManager.find(Registration.class, registration.getId());

        assertThat(deletedRegistration).isNull();

    }

    private Registration createNewRegistration(String cpf) {

        return Registration.builder()
                .name("Caroline")
                .dateOfRegistration("01/04/2022")
                .cpf(cpf)
                .build();
    }
}
