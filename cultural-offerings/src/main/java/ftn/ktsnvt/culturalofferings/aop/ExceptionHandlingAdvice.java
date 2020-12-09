package ftn.ktsnvt.culturalofferings.aop;

import ftn.ktsnvt.culturalofferings.model.exceptions.*;
import ftn.ktsnvt.culturalofferings.model.exceptions.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlingAdvice {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Error> getError(EntityNotFoundException e){
        String message = "Entity " + e.getClassObject().getName() + " with id " + e.getId() + " not found! Make sure your request is valid!";
        return new ResponseEntity<Error>(new Error(message), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Error> getError(UserNotFoundException e){
        String message = "User with email " + e.getEmail() + " not found! Are you sure the user is registered?";
        return new ResponseEntity<Error>(new Error(message), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserEmailAlreadyVerifiedException.class)
    public ResponseEntity<Error> getError(UserEmailAlreadyVerifiedException e){
        String message = "User with email " + e.getEmail() + " has already had his account verified.";
        return new ResponseEntity<Error>(new Error(message), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(VerificationTokenNotFoundException.class)
    public ResponseEntity<Error> getError(VerificationTokenNotFoundException e){
        String message = "Token " + e.getToken() + " has not been found. Are you sure the user is registered?";
        return new ResponseEntity<Error>(new Error(message), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RequestBodyBindingFailedException.class)
    public ResponseEntity<Error> getError(RequestBodyBindingFailedException e){
        String message = "Request body binding failed for entity " + e.getClassObject().getName()
                + ". Failed field: " + e.getFieldName()
                + ". Message: " + e.getMessage();
        return new ResponseEntity<Error>(new Error(message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ModelConstraintViolationException.class)
    public ResponseEntity<Error> getError(ModelConstraintViolationException e){
        String message = "Model violation for entity " + e.getClassObject().getName()
                + ". Message: " + e.getMessage();
        return new ResponseEntity<Error>(new Error(message), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}