package org.ssafy.demotravel.travels;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TravelService {

    private final TravelRepository travelRepository;

    public List<Travel> findAll(){
        return this.travelRepository.findAll();
    }

    public Optional<Travel> findById(Long id){

        return this.travelRepository.findById(id);
    }
}
