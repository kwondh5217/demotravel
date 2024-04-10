package org.ssafy.demotravel.exception;

import org.springframework.hateoas.EntityModel;
import org.ssafy.demotravel.IndexController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ErrorResource extends EntityModel<ErrorCode> {

    public ErrorResource(ErrorCode errorCode){
        super(errorCode);
        add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
    }

}
