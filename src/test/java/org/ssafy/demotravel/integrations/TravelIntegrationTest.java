package org.ssafy.demotravel.integrations;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.ssafy.demotravel.travels.Travel;
import org.ssafy.demotravel.travels.TravelRepository;

import java.util.stream.IntStream;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TravelIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TravelRepository travelRepository;


    @DisplayName("Travel 첫번째 페이지로 10개를 조회하는 테스트")
    @Test
    void findAll() throws Exception {
        // given
        IntStream.range(0, 30).forEach(i -> {
            Travel travel = new Travel();
            travel.setName("test" + i);
            this.travelRepository.save(travel);
        });

        // when
        ResultActions resultActions = this.mockMvc.perform(get("/api/travels")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "name,DESC"));

        // then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("_embedded.travelList[0].name").exists())
                .andExpect(jsonPath("_embedded.travelList", hasSize(10)))
        ;
    }

    @DisplayName("Travel ID로 조회하는 테스트")
    @Test
    void findById() throws Exception {
        // given
        Travel travel = new Travel();
        String name = "test";
        travel.setName(name);
        Travel save = this.travelRepository.save(travel);

        // when
        ResultActions resultActions = this.mockMvc.perform(
                get("/api/travels/{id}", save.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON));

        // then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("_links.self").exists())
        ;
    }

    @DisplayName("Travel조회 요청이 실패하는 테스트")
    @Test
    void getTravel_notFound() throws Exception {
        // when & then
        this.mockMvc.perform(get("/api/travels/1232"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
