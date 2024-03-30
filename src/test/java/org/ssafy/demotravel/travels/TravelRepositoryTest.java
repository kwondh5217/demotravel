package org.ssafy.demotravel.travels;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TravelRepositoryTest {

    @Autowired
    TravelRepository travelRepository;

    @DisplayName("Travel 두번째 페이지에서 10개 조회하는테스트")
    @Test
    void findAll(){
        // given
        IntStream.range(0, 30).forEach(i -> {
            Travel travel = new Travel();
            travel.setName("test" + i);
            this.travelRepository.save(travel);
        });
        PageRequest pageRequest = PageRequest.of(1, 10);
        // when
        var travels = this.travelRepository.findAll(pageRequest);

        // then
        assertThat(travels).isNotEmpty();
        assertThat(travels.getNumberOfElements()).isEqualTo(10);
    }

    @DisplayName("Travel ID로 조회하는 테스트")
    @Test
    void findById(){
        // given
        Travel travel = new Travel();
        String name = "test";
        travel.setName(name);
        Travel save = this.travelRepository.save(travel);

        // when
        Optional<Travel> optionalTravel = this.travelRepository.findById(save.getId());

        // then
        assertThat(optionalTravel).isNotEmpty();
        assertThat(optionalTravel.get().getName()).isEqualTo(name);
    }

    @DisplayName("존재하지 않는 Travel 요청에 null을 반환하는 테스트")
    @Test
    void findById_notFound(){
        // given
        Long id = 154234L;
        // when
        Optional<Travel> optionalTravel = this.travelRepository.findById(id);

        // then
        assertThat(optionalTravel).isEmpty();
    }



}