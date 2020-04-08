package pl.agh.product.catalog.application.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.agh.product.catalog.common.exception.BadRequestException;
import pl.agh.product.catalog.common.exception.CustomException;
import pl.agh.product.catalog.common.exception.NotFoundException;
import pl.agh.product.catalog.common.response.CustomExceptionResponse;

@ControllerAdvice
public class CustomExceptionControllerAdvice {

    @ExceptionHandler(BadRequestException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomExceptionResponse handleBadRequestException(CustomException se) {
        return new CustomExceptionResponse(se.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CustomExceptionResponse handleNotFoundException(CustomException se) {
        return new CustomExceptionResponse(se.getMessage());
    }
}
