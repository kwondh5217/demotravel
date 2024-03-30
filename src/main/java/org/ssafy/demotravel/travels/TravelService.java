package org.ssafy.demotravel.travels;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TravelService {

    private final TravelRepository travelRepository;

    public Page<Travel> findAll(Pageable pageable){
        return this.travelRepository.findAll(pageable);
    }

    public Optional<Travel> findById(Long id){

        return this.travelRepository.findById(id);
    }
}
