package com.beryllinus.hotel_service.contoller;

import com.beryllinus.hotel_service.dto.UserIdentification;
import com.beryllinus.hotel_service.dto.response.PersonDTO;
import com.beryllinus.hotel_service.service.PersonService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@RestController
@RequestMapping("/api/people")
public class PersonController {
    private static final Set<String> ALLOWED_SORT_FIELDS =
            Set.of(
                    "firstName",
                    "lastName",
                    "createdAt",
                    "updatedAt"
            );

    private static final Set<String> ALLOWED_DIRECTIONS =
            Set.of("asc", "desc");

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping("/search")
    public ResponseEntity<PersonDTO> searchPerson(@RequestBody UserIdentification userIdentification) {
        return ResponseEntity.ok(personService.getPersonByUserIdentification(userIdentification));
    }

    @GetMapping
    public ResponseEntity<Page<PersonDTO>> getPeople(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "updatedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            sortBy = "updatedAt";
        }
        page = Math.max(page, 0);
        size = Math.clamp(size, 1, 25);

        direction = direction.toLowerCase();
        if (!ALLOWED_DIRECTIONS.contains(direction)) {
            direction = "desc";
        }
        return ResponseEntity.ok(personService.getPeople(page, size, sortBy, direction));
    }

    @PostMapping
    public ResponseEntity<PersonDTO> createPerson(@RequestBody PersonDTO personDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(personService.createPerson(personDTO));
    }

    //TODO
    //    @GetMapping
    //    public ResponseEntity<PersonResponse> searchPerson(@RequestBody UserSearch userSearch) {
    //        return ResponseEntity.ok(personService.searchPerson(userSearch));
    //
    //    }

}
