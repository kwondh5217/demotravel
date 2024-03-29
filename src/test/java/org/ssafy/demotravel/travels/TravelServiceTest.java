package org.ssafy.demotravel.travels;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @DisplayName("Travel 전체를 조회하는 테스트")
    @Test
    void findAll() {
        // given
        List<Travel> travels = new ArrayList<>();
        IntStream.range(0, 10).forEach(i -> {
            Travel travel = new Travel();
            travel.setName("test" + i);
            travels.add(travel);
        });
        when(this.travelRepository.findAll()).thenReturn(travels);

        // when
        List<Travel> travelList = this.travelService.findAll();

        // then
        assertThat(travelList.isEmpty()).isFalse();
        assertThat(travelList.size()).isEqualTo(10);
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