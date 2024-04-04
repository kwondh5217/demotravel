package org.ssafy.demotravel.travels;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
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

    @DisplayName("위도 경도 범위 내의 여행지 정보 조회하기")
    @Test
    void getTravelsByLocationInfo(){
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
        this.travelRepository.save(travel);

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
        this.travelRepository.save(outOfTravel);

        BigDecimal northLatitude = BigDecimal.valueOf(38.888780630714486);
        BigDecimal eastLongitude = BigDecimal.valueOf(136.7436851450521);
        BigDecimal southLatitude = BigDecimal.valueOf(37.648990187475206);
        BigDecimal westLongitude = BigDecimal.valueOf(124.14474958053674);
        PageRequest pageRequest = PageRequest.of(0, 10);
        // when
        Page<Travel> byCoordinates = this.travelRepository.findByCoordinates(northLatitude, southLatitude,
                eastLongitude, westLongitude, pageRequest);

        // then
        assertThat(byCoordinates.getSize()).isEqualTo(1);
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
