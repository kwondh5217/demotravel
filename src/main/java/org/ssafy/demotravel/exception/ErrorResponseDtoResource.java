package org.ssafy.demotravel.exception;

import org.springframework.hateoas.EntityModel;
import org.ssafy.demotravel.IndexController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ErrorResponseDtoResource extends EntityModel<ErrorResponseDto> {

    public ErrorResponseDtoResource(ErrorResponseDto errorResponseDto){
        super(errorResponseDto);
        add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
    }

}
