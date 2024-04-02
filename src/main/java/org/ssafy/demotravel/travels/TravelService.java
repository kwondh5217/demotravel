package org.ssafy.demotravel.travels;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TravelService {

    private final TravelRepository travelRepository;

    public Page<Travel> findBySido(int sidoCode, Pageable pageable){
        return travelRepository.findByTravelSidoCode(sidoCode, pageable);
    }
    public Page<Travel> findByGugun(int gugunCode, Pageable pageable){
        return travelRepository.findByTravelGugunCode(gugunCode, pageable);
    }

    public Page<Travel> findByKeyword(String keyword, Pageable pageable){
        return travelRepository.findByTravelTitleContaining(keyword, pageable);
    }

    public Page<Travel> findAll(Pageable pageable){
        return this.travelRepository.findAll(pageable);
    }

    public Optional<Travel> findById(Long id){
        return this.travelRepository.findById(id);
    }

    public Page<Travel> findByCoordinates(BigDecimal northLatitude, BigDecimal southLatitude,
                                          BigDecimal eastLongitude, BigDecimal westLongitude, Pageable pageable){
        return this.travelRepository.findByCoordinates(northLatitude, southLatitude,
                eastLongitude, westLongitude, pageable);
    }
}
