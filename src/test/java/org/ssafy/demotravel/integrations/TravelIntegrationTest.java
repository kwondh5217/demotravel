package org.ssafy.demotravel.integrations;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.ssafy.demotravel.travels.Travel;
import org.ssafy.demotravel.travels.TravelRepository;

import java.math.BigDecimal;
import java.util.stream.IntStream;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(RestDocumentationExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class TravelIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TravelRepository travelRepository;


    @BeforeEach
    void setup(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentationContextProvider){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentationContextProvider)
                        .operationPreprocessors()
                        .withRequestDefaults(modifyUris().host("localhost").removePort(), prettyPrint())
                        .withResponseDefaults(modifyUris().host("localhost").removePort(), prettyPrint()))
                .build();
    }

    @DisplayName("Travel pageable 테스트")
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
                .param("sort", "travelTitle,DESC"));

        // then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE+";charset=UTF-8"))
                .andExpect(jsonPath("_embedded.travelList[0].travelTitle").exists())
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
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE+";charset=UTF-8"))
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

    @DisplayName("위도 경도 범위 내의 여행지 정보 조회하기")
    @Test
    void getTravelsByLocationInfo() throws Exception {
        // given
        Travel travel = Travel.builder()
                .id(125589L)
                .travelTitle("강원도 양양군 강현면 일출로 42")
                .travelZipcode("25009")
                .travelFirstImage("http://tong.visitkorea.or.kr/cms/resource/55/763455_image2_1.jpg")
                .travelFirstImage2("http://tong.visitkorea.or.kr/cms/resource/55/763455_image3_1.jpg")
                .travelSidoCode(32)
                .travelGugunCode(7)
                .travelLatitude(BigDecimal.valueOf(38.0610181000000000))
                .travelLongitude(BigDecimal.valueOf(128.63138220000000000))
                .build();
        Travel outOfTravel = Travel.builder()
                .id(125521L)
                .travelTitle("강원도 양양군 강현면 일출로 42")
                .travelZipcode("25009")
                .travelFirstImage("http://tong.visitkorea.or.kr/cms/resource/55/763455_image2_1.jpg")
                .travelFirstImage2("http://tong.visitkorea.or.kr/cms/resource/55/763455_image3_1.jpg")
                .travelSidoCode(32)
                .travelGugunCode(7)
                .travelLatitude(BigDecimal.valueOf(35.0610181000000000))
                .travelLongitude(BigDecimal.valueOf(128.63138220000000000))
                .build();
        this.travelRepository.save(travel);
        this.travelRepository.save(outOfTravel);

        // when
        ResultActions resultActions = this.mockMvc.perform(get("/api/travels/coordinate")
                .param("northLatitude", "35.54851698585924")
                .param("southLatitude", "33.86605388558357")
                .param("eastLongitude", "132.27023703996352")
                .param("westLongitude", "120.30370775572416")
                .param("page", "0")
                .param("size", "10"));

        // then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page.totalElements").value(1));
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
