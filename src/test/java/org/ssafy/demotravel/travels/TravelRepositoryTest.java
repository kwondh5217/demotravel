package org.ssafy.demotravel.travels;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
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
            travel.setTravelTitle("test" + i);
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
        travel.setTravelTitle(name);
        Travel save = this.travelRepository.save(travel);

        // when
        Optional<Travel> optionalTravel = this.travelRepository.findById(save.getId());

        // then
        assertThat(optionalTravel).isNotEmpty();
        assertThat(optionalTravel.get().getTravelTitle()).isEqualTo(name);
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

    @DisplayName("Travel sido 코드로 조회하기")
    @Test
    void findBySidoCode() {
        // given
        int sidoCode = 12345;
        PageRequest pageRequest = PageRequest.of(0, 10);
        generateTravels(sidoCode);

        // when
        Page<Travel> bySido = this.travelRepository.findByTravelSidoCode(sidoCode, pageRequest);

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
        generateTravels(gugunCode);

        // when
        Page<Travel> byGugun = this.travelRepository.findByTravelGugunCode(gugunCode, pageRequest);

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
        IntStream.range(0, 30).forEach(i -> {
            Travel travel = new Travel();
            travel.setTravelTitle("test" + i + keyword);
            this.travelRepository.save(travel);
        });

        // when
        Page<Travel> byKeyword = this.travelRepository.findByTravelTitleContaining(keyword, pageRequest);

        //then
        assertThat(byKeyword).isNotEmpty();
        assertThat(byKeyword).size().isEqualTo(10);
        assertThat(byKeyword.get().findFirst().get().getTravelTitle()).contains(keyword);
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