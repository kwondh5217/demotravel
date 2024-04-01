package org.ssafy.demotravel.travels;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/sido")
    public ResponseEntity findBysido(@RequestParam("sidoCode") int sidoCode,
                                     PagedResourcesAssembler <Travel> assembler,
                                     Pageable pageable){
        Page<Travel> bySido = travelService.findBySido(sidoCode, pageable);
        var bySidoResources = assembler.toModel(bySido, t -> new TravelResource(t));

        return ResponseEntity.ok().body(bySidoResources);
    }

    @GetMapping("/gugun")
    public ResponseEntity findByGugun(@RequestParam("gugunCode") int gugunCode,
                                     PagedResourcesAssembler <Travel> assembler,
                                     Pageable pageable){
        Page<Travel> byGugun = travelService.findByGugun(gugunCode, pageable);
        var byGugunResources = assembler.toModel(byGugun, t -> new TravelResource(t));

        return ResponseEntity.ok().body(byGugunResources);
    }

    @GetMapping("/keyword")
    public ResponseEntity findByKeyword(@RequestParam("keyword") String keyword,
                                      PagedResourcesAssembler <Travel> assembler,
                                      Pageable pageable){
        Page<Travel> byKeyword = travelService.findByKeyword(keyword, pageable);
        var byKeywordResources = assembler.toModel(byKeyword, t -> new TravelResource(t));

        return ResponseEntity.ok().body(byKeywordResources);
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
