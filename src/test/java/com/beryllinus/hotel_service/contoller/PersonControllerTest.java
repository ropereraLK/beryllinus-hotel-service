package com.beryllinus.hotel_service.contoller;

import com.beryllinus.hotel_service.dto.response.PersonDTO;
import com.beryllinus.hotel_service.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PersonService personService;

    @Test
    void shouldReturnPeoplePage() throws Exception {

        PersonDTO response = new PersonDTO();

        response.setFirstName("Rohan");
        response.setLastName("Perera");

        Page<PersonDTO> page =
                new PageImpl<>(List.of(response));

        when(personService.getPeople(
                0,
                10,
                "updatedAt",
                "desc"
        )).thenReturn(page);

        mockMvc.perform(
                        get("/api/people")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }
}