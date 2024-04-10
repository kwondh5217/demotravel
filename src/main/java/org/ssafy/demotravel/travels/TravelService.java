package org.ssafy.demotravel.travels;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ssafy.demotravel.exception.CustomErrorException;
import org.ssafy.demotravel.exception.ErrorCode;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TravelService {

    private final TravelRepository travelRepository;

    public Page<Travel> findBySido(int sidoCode, Pageable pageable){
        Page<Travel> byTravelSidoCode = travelRepository.findByTravelSidoCode(sidoCode, pageable);
        if(byTravelSidoCode.isEmpty()){
            throw new CustomErrorException(ErrorCode.InvalidSidoCode);
        }
        return byTravelSidoCode;
    }
    public Page<Travel> findByGugun(int gugunCode, Pageable pageable){
        Page<Travel> byTravelGugunCode = travelRepository.findByTravelGugunCode(gugunCode, pageable);
        if(byTravelGugunCode.isEmpty()){
            throw new CustomErrorException(ErrorCode.InvalidGugunCode);
        }

        return byTravelGugunCode;
    }

    public Page<Travel> findByKeyword(String keyword, Pageable pageable){
        Page<Travel> byTravelTitleContaining = travelRepository.findByTravelTitleContaining(keyword, pageable);
        if(byTravelTitleContaining.isEmpty()){
            throw new CustomErrorException(ErrorCode.InvalidKeyword);
        }

        return byTravelTitleContaining;
    }

    public Page<Travel> findAll(Pageable pageable){
        return this.travelRepository.findAll(pageable);
    }

    public Travel findById(Long id){
        return this.travelRepository.findById(id)
                .orElseThrow(() -> new CustomErrorException(ErrorCode.invalidTravelId));
    }

    public Page<Travel> findByCoordinates(BigDecimal northLatitude, BigDecimal southLatitude,
                                          BigDecimal eastLongitude, BigDecimal westLongitude, Pageable pageable){
        return this.travelRepository.findByCoordinates(northLatitude, southLatitude,
                eastLongitude, westLongitude, pageable);
    }
}
