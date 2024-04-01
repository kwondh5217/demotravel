package org.ssafy.demotravel.integrations;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.ssafy.demotravel.travels.Travel;
import org.ssafy.demotravel.travels.TravelRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

<<<<<<< HEAD
import static org.hamcrest.Matchers.hasSize;
=======
import static org.mockito.Mockito.when;
>>>>>>> my-new-branch
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
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
            travel.setTravelTitle("test" + i);
            this.travelRepository.save(travel);
        });

        // when
        ResultActions resultActions = this.mockMvc.perform(get("/api/travels")
                .param("page", "0")
<<<<<<< HEAD
                .param("size", "10")
                .param("sort", "name,DESC"));
=======
                .param("sort", "travelTitle,DESC"));
>>>>>>> my-new-branch

        // then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
<<<<<<< HEAD
                .andExpect(jsonPath("_embedded.travelList[0].name").exists())
                .andExpect(jsonPath("_embedded.travelList", hasSize(10)))
=======
                .andExpect(jsonPath("_embedded.travelList[0].travelTitle").exists())
>>>>>>> my-new-branch
        ;
    }

    @DisplayName("Travel ID로 조회하는 테스트")
    @Test
    void findById() throws Exception {
        // given
        Travel travel = new Travel();
        String name = "test";
        travel.setTravelTitle(name);
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
                .andExpect(jsonPath("travelTitle").exists())
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


    @DisplayName("sido code로 여행지 정보 조회하기")
    @Test
    void findBySidoCode() throws Exception {
        // given
        int sidoCode = 12345;
        generateTravels(sidoCode);

        // when
        ResultActions resultActions = this.mockMvc.perform(get("/api/travels/sido")
                .param("sidoCode", String.valueOf(sidoCode))
                .param("page", "0")
                .param("size", "10"));

        // then
        resultActions.andDo(print())
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.travelList[*].id").exists());
    }

    @DisplayName("gugun code로 여행지 정보 조회하기")
    @Test
    void findByGugunCode() throws Exception {
        // given
        int gugunCode = 12345;
        generateTravels(gugunCode);

        // when
        ResultActions resultActions = this.mockMvc.perform(get("/api/travels/gugun")
                .param("gugunCode", String.valueOf(gugunCode))
                .param("page", "0")
                .param("size", "10"));

        // then
        resultActions.andDo(print())
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.travelList[*].id").exists());
    }

    @DisplayName("키워드가 포함된 여행지 정보 조회하기")
    @Test
    void findByKeyword() throws Exception {
        // given
        String keyword = "부산";
        IntStream.range(0, 30).forEach(i -> {
            Travel travel = new Travel();
            travel.setTravelTitle("test" + i + keyword);
            this.travelRepository.save(travel);
        });
        // when
        ResultActions resultActions = this.mockMvc.perform(get("/api/travels/keyword")
                .param("keyword", keyword)
                .param("page", "0")
                .param("size", "10"));

        // then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.travelList[*].travelTitle").exists());
    }






    private void generateTravels(int code){
        IntStream.range(0, 30).forEach(i -> {
            Travel travel = new Travel();
            travel.setTravelTitle("test" + i);
            travel.setTravelSidoCode(code);
            travel.setTravelGugunCode(code);
            this.travelRepository.save(travel);
        });
    }
}
