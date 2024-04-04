package org.ssafy.demotravel.travels;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface TravelRepository extends JpaRepository<Travel, Long> {
    Optional<Travel> findById(Long id);
    Page<Travel> findByTravelSidoCode(int sido, Pageable pageable);
    Page<Travel> findByTravelGugunCode(int gugun, Pageable pageable);
    Page<Travel> findByTravelTitleContaining(String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM travels " +
            "WHERE travel_latitude <= :northLatitude AND travel_latitude >= :southLatitude " +
            "AND travel_longitude <= :eastLongitude AND travel_longitude >= :westLongitude",
            nativeQuery = true)
    Page<Travel> findByCoordinates(@Param("northLatitude") BigDecimal northLatitude,
                                   @Param("southLatitude") BigDecimal southLatitude,
                                   @Param("eastLongitude") BigDecimal eastLongitude,
                                   @Param("westLongitude") BigDecimal westLongitude, Pageable pageable);
}
