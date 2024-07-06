package com.tanvir.features.person.application.port.in;

import com.google.gson.Gson;
import com.tanvir.core.util.CommonFunctions;
import com.tanvir.features.person.adapter.out.database.entities.PersonEntity;
import com.tanvir.features.person.adapter.out.database.repositories.PersonRepository;
import com.tanvir.features.person.application.port.out.PersonPersistencePort;
import com.tanvir.features.person.domain.Person;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StreamUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;


@SpringBootTest
@ActiveProfiles("integration-test")
class PersonUseCaseTest {

    @MockBean
    private PersonRepository repository;

    @Autowired
    private PersonUseCase personUseCase;

    private Gson gson;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(personUseCase);
    }

    @Test
    void getPersonById() throws IOException {
        // Given
        String personId = "MRA-PER-0001";
        String mfiId = "M1001";
        Person expectedPerson = createTestPerson();

        // Mock  any external dependencies
        Mockito.when(repository.findByMfiIdAndPersonId(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(createTestPersonMono());

        // When
        Mono<Person> resultMono = personUseCase.getPersonByID(mfiId, personId);

        // Then
        StepVerifier.create(resultMono)
                .consumeNextWith(person -> {
                    org.assertj.core.api.Assertions.assertThat(person)
                            .usingRecursiveComparison()
                            .ignoringCollectionOrder()
                            .ignoringFieldsOfTypes(LocalDate.class)
                            .isEqualTo(expectedPerson);
                })
                .verifyComplete();
    }

    private Person createTestPerson() throws IOException {
        ClassPathResource resource = new ClassPathResource("json/person/person.json");
        String jsonContent = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        gson = CommonFunctions.buildGson(jsonContent);
        return gson.fromJson(jsonContent, Person.class);
    }
    private Mono<PersonEntity> createTestPersonMono() throws IOException {
        ClassPathResource resource = new ClassPathResource("json/person/person.json");
        String jsonContent = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        gson = CommonFunctions.buildGson(jsonContent);
        return Mono.just(gson.fromJson(jsonContent, PersonEntity.class));
    }

}