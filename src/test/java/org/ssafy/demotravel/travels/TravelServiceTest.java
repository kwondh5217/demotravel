package org.ssafy.demotravel.travels;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@DisplayName("TravelService")
@ExtendWith(MockitoExtension.class)
class TravelServiceTest {
    @Mock
    TravelRepository travelRepository;

    private TravelService travelService;

    @BeforeEach
    void setup(){
        this.travelService = new TravelService(travelRepository);
    }

    @DisplayName("Travel 두번째 페이지에서 10개 조회하는 테스트")
    @Test
    void findAll() {
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
        when(this.travelRepository.findAll(pageRequest)).thenReturn(travelPage);

        // when
        Page<Travel> travelList = this.travelService.findAll(pageRequest);

        // then
        assertThat(travelList.isEmpty()).isFalse();
        assertThat(travelList.getNumberOfElements()).isEqualTo(10);
    }

    @DisplayName("Travel ID로 조회하는 테스트")
    @Test
    void findById() {
        // given
        Travel travel = new Travel();
        travel.setId(1L);
        travel.setTravelTitle("test");
        when(this.travelRepository.findById(travel.getId()))
                .thenReturn(Optional.of(travel));

        // when
        Optional<Travel> optionalTravel = this.travelService.findById(travel.getId());

        // then
        assertThat(optionalTravel.isEmpty()).isFalse();
        assertThat(optionalTravel.get().getTravelTitle()).isEqualTo(travel.getTravelTitle());
    }

    @DisplayName("Travel sido 코드로 조회하기")
    @Test
    void findBySidoCode() {
        // given
        int sidoCode = 12345;
        PageRequest pageRequest = PageRequest.of(0, 10);
        PageImpl<Travel> travels = generateTravels(sidoCode, pageRequest);
        when(this.travelRepository.findByTravelSidoCode(sidoCode, pageRequest)).thenReturn(travels);

        // when
        Page<Travel> bySido = this.travelService.findBySido(sidoCode, pageRequest);

        //then
        assertThat(bySido).isNotEmpty();
        assertThat(bySido).size().isEqualTo(10);
        assertThat(bySido.get().findFirst().get().getTravelSidoCode()).isEqualTo(sidoCode);
    }

    @DisplayName("Travel gugun 코드로 조회하기")
    @Test
    void findByGugunCode() {
        // given
        int gugunCode = 12345;
        PageRequest pageRequest = PageRequest.of(0, 10);
        PageImpl<Travel> travels = generateTravels(gugunCode, pageRequest);
        when(this.travelRepository.findByTravelGugunCode(gugunCode, pageRequest)).thenReturn(travels);

        // when
        Page<Travel> byGugun = this.travelService.findByGugun(gugunCode, pageRequest);

        //then
        assertThat(byGugun).isNotEmpty();
        assertThat(byGugun).size().isEqualTo(10);
        assertThat(byGugun.get().findFirst().get().getTravelGugunCode()).isEqualTo(gugunCode);
    }

    @DisplayName("title에 keyword가 포함된 Travel 조회하기")
    @Test
    void findByKeyword() {
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

        when(this.travelRepository.findByTravelTitleContaining(keyword, pageRequest)).thenReturn(travelsPages);

        // when
        Page<Travel> byKeyword = this.travelService.findByKeyword(keyword, pageRequest);

        //then
        assertThat(byKeyword).isNotEmpty();
        assertThat(byKeyword).size().isEqualTo(10);
        assertThat(byKeyword.get().findFirst().get().getTravelTitle()).contains(keyword);
    }



    private PageImpl<Travel> generateTravels(int code, PageRequest pageRequest){
        List<Travel> travels = new ArrayList<>();
        IntStream.range(0, 30).forEach(i -> {
            Travel travel = new Travel();
            travel.setTravelTitle("test" + i);
            travel.setTravelSidoCode(code);
            travel.setTravelGugunCode(code);
            travels.add(travel);
        });

        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), travels.size());
        return new PageImpl<>(travels.subList(start, end), pageRequest, travels.size());
    }



}
