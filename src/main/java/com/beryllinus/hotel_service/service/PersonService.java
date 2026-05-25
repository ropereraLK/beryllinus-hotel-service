package com.beryllinus.hotel_service.service;

import com.beryllinus.hotel_service.dto.UserIdentification;
import com.beryllinus.hotel_service.dto.response.PersonDTO;
import com.beryllinus.hotel_service.enumuration.IdentificationType;
import com.beryllinus.hotel_service.exceptions.PersonNotFoundException;
import com.beryllinus.hotel_service.mapper.PersonMapper;
import com.beryllinus.hotel_service.model.Person;
import com.beryllinus.hotel_service.repository.PersonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    public PersonService(PersonRepository personRepository,
                         PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }

    public PersonDTO getPersonByUserIdentification(final UserIdentification userIdentification) {
        Optional<Person> person = switch (userIdentification.identificationType()) {
            case IdentificationType.PASSPORT ->
                    personRepository.findByCountryAndPassportIdentificationNumber(userIdentification.issuingCountryCode(), userIdentification.identificationDocNo());
            case IdentificationType.NIC -> personRepository.findByNic(userIdentification.identificationDocNo());
            case IdentificationType.DRIVING_LICENCE ->
                    personRepository.findByDrivingLicence(userIdentification.identificationDocNo());
            default -> throw new RuntimeException();
        };
        return person
                .map(personMapper::toPersonResponse)
                .orElseThrow(PersonNotFoundException::new);
    }

    //TODO
    //    public PersonResponse searchPerson(UserSearch userSearch) {
    //        return null;
    //    }

    public Page<PersonDTO> getPeople(int page, int size, String sortBy, String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable =
                PageRequest.of(page, size, sort);

        return personRepository.findAll(pageable)
                .map(personMapper::toPersonResponse);
    }

    /***
     * @param personDTO
     * @return
     */
    public PersonDTO createPerson(PersonDTO personDTO) {
        return personMapper.toPersonResponse(personRepository.save(personMapper.toPerson(personDTO)));

    }
}
