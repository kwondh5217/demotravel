package org.ssafy.demotravel.travels;

import org.springframework.hateoas.EntityModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class TravelResource extends EntityModel<Travel> {
    public TravelResource(Travel travel){
        super(travel);
        add(linkTo(TravelController.class).slash(travel.getId()).withSelfRel());
    }


}
