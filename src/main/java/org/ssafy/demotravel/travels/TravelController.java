package org.ssafy.demotravel.travels;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/travels")
public class TravelController {

    private final TravelService travelService;

    @GetMapping
    public ResponseEntity getTravels(Pageable pageable, PagedResourcesAssembler<Travel> assembler) {
        Page<Travel> travels = travelService.findAll(pageable);
        var travelResources = assembler.toModel(travels, t -> new TravelResource(t));

        return ResponseEntity.ok().body(travelResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity getTravel(@PathVariable("id") Long id) {
        Optional<Travel> optionalTravel = travelService.findById(id);
        if(optionalTravel.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Travel travel = optionalTravel.get();
        TravelResource resource = new TravelResource(travel);

        return ResponseEntity.ok(resource);
    }


}
