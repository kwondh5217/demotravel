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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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
            travel.setName("test" + i);
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
        travel.setName("test");
        when(this.travelRepository.findById(travel.getId()))
                .thenReturn(Optional.of(travel));

        // when
        Optional<Travel> optionalTravel = this.travelService.findById(travel.getId());

        // then
        assertThat(optionalTravel.isEmpty()).isFalse();
        assertThat(optionalTravel.get().getName()).isEqualTo(travel.getName());
    }



}