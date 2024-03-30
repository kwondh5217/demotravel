package org.ssafy.demotravel.travels;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("TravelController")
@WebMvcTest
class TravelControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean(name = "travelService")
    TravelService travelService;

    @DisplayName("Travel 전체를 조회하는 테스트")
    @Test
    void findAll() throws Exception {
        // given
        List<Travel> travels = new ArrayList<>();
        IntStream.range(0, 10).forEach(i -> {
            Travel travel = new Travel();
            travel.setName("test" + i);
            travels.add(travel);
        });
        when(this.travelService.findAll()).thenReturn(travels);

        // when & then
        this.mockMvc.perform(get("/api/travels"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").exists());
    }

    @DisplayName("Travel ID로 조회하는 테스트")
    @Test
    void callController() throws Exception {
        // given
        Travel travel = new Travel();
        travel.setId(1L);
        travel.setName("test");

        when(this.travelService.findById(travel.getId())).thenReturn(Optional.of(travel));

        // when & then
        this.mockMvc.perform(get("/api/travels/{id}", travel.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("name").exists());
    }

    @DisplayName("Travel조회 요청이 실패하는 테스트")
    @Test
    void callController_notFound() throws Exception {
        Optional<Travel> empty = Optional.empty();
        // given
        when(this.travelService.findById(anyLong())).thenReturn(empty);

        // when & then
        this.mockMvc.perform(get("/api/travels/1232"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}