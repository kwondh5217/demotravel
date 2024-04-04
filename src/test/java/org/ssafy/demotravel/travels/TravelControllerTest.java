package org.ssafy.demotravel.travels;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@DisplayName("TravelController")
@WebMvcTest(TravelController.class)
class TravelControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean(name = "travelService")
    TravelService travelService;

    @DisplayName("Travel 두번째 페이지에서 10개 조회하는 테스트")
    @Test
    void findAll() throws Exception {
        // given
        List<Travel> travels = new ArrayList<>();
        IntStream.range(0, 30).forEach(i -> {
            Travel travel = new Travel();
            travel.setTravelTitle("test" + i);
            travels.add(travel);
        });
        PageRequest pageRequest = PageRequest.of(1, 10);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), travels.size());
        PageImpl<Travel> travelPage = new PageImpl<>(travels.subList(start, end), pageRequest, travels.size());

        when(this.travelService.findAll(any(Pageable.class))).thenReturn(travelPage);

        // when & then
        this.mockMvc.perform(get("/api/travels")
                        .param("page", "1")
                        .param("sort", "name,DESC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("_embedded.travelList[0].travelTitle").exists());
    }

    @DisplayName("Travel ID로 조회하는 테스트")
    @Test
    void callController() throws Exception {
        // given
        Travel travel = new Travel();
        travel.setId(1L);
        travel.setTravelTitle("test");

        when(this.travelService.findById(travel.getId())).thenReturn(Optional.of(travel));

        // when & then
        this.mockMvc.perform(get("/api/travels/{id}", travel.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("travelTitle").exists());
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

    @DisplayName("sido code로 여행지 정보 조회하기")
    @Test
    void findBySidoCode() throws Exception {
        // given
        int sidoCode = 12345;
        PageRequest pageRequest = PageRequest.of(0, 10);
        PageImpl<Travel> travels = generateTravels(sidoCode, pageRequest);

        when(this.travelService.findBySido(sidoCode, pageRequest)).thenReturn(travels);
        // when
        ResultActions resultActions = this.mockMvc.perform(get("/api/travels/sido")
                .param("sidoCode", String.valueOf(sidoCode))
                .param("page", "0")
                .param("size", "10"));

        // then
        resultActions.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("gugun code로 여행지 정보 조회하기")
    @Test
    void findByGugnCode() throws Exception {
        // given
        int gugunCode = 12345;
        PageRequest pageRequest = PageRequest.of(0, 10);
        PageImpl<Travel> travels = generateTravels(gugunCode, pageRequest);

        when(this.travelService.findByGugun(gugunCode, pageRequest)).thenReturn(travels);
        // when
        ResultActions resultActions = this.mockMvc.perform(get("/api/travels/gugun")
                .param("gugunCode", String.valueOf(gugunCode))
                .param("page", "0")
                .param("size", "10"));

        // then
        resultActions.andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("키워드가 포함된 여행지 정보 조회하기")
    @Test
    void findByKeyword() throws Exception {
        // given
        String keyword = "부산";
        PageRequest pageRequest = PageRequest.of(0, 10);
        List<Travel> travels = new ArrayList<>();
        IntStream.range(0, 30).forEach(i -> {
            Travel travel = new Travel();
            travel.setTravelTitle("test" + i + keyword);
            travels.add(travel);
        });

        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), travels.size());
        PageImpl<Travel> travelsPages = new PageImpl<>(travels.subList(start, end), pageRequest, travels.size());


        when(this.travelService.findByKeyword(keyword, pageRequest)).thenReturn(travelsPages);
        // when
        ResultActions resultActions = this.mockMvc.perform(get("/api/travels/keyword")
                .param("keyword", keyword)
                .param("page", "0")
                .param("size", "10"));

        // then
        resultActions.andDo(print())
                .andExpect(status().isOk());
    }


    private PageImpl<Travel> generateTravels(int code, PageRequest pageRequest){
        List<Travel> travels = new ArrayList<>();
        IntStream.range(0, 30).forEach(i -> {
            Travel travel = new Travel();
            travel.setTravelTitle("test" + i);
            travel.setTravelSidoCode(code);
            travels.add(travel);
        });

        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), travels.size());
        return new PageImpl<>(travels.subList(start, end), pageRequest, travels.size());
    }

}