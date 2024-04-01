package org.ssafy.demotravel.travels;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TravelRepository extends JpaRepository<Travel, Long> {
    Optional<Travel> findById(Long id);
    Page<Travel> findByTravelSidoCode(int sido, Pageable pageable);
    Page<Travel> findByTravelGugunCode(int gugun, Pageable pageable);
    Page<Travel> findByTravelTitleContaining(String keyword, Pageable pageable);

}
