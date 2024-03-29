package org.ssafy.demotravel.travels;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/travels")
public class TravelController {

    private final TravelService travelService;

    @GetMapping
    public ResponseEntity getTravels() {
        List<Travel> travels = travelService.findAll();

        return ResponseEntity.ok().body(travels);
    }

    @GetMapping("/{id}")
    public ResponseEntity getTravel(@PathVariable("id") Long id) {
        Optional<Travel> optionalTravel = travelService.findById(id);
        if(optionalTravel.isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        Travel travel = optionalTravel.get();
        TravelResource resource = new TravelResource(travel);

        return ResponseEntity.ok(resource);
    }


}
