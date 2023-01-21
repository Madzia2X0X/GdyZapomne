package pl.gdyzapomne.blog.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

/**
 * Klasa implementująca obsługę wyjątków.
 */
@ControllerAdvice
public class ExceptionHandlerService extends ExceptionHandlerExceptionResolver {

    private StringBuilder responseBuilder;

    /**
     * Obsługuje wyjątek MethodArgumentNotValid.
     * Wyjątek ten występuje gdy aplikacja nie powiedzie się walidacja argumentu metody.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            responseBuilder.append("Validation error at field: " + fieldName + " - Error message: " + errorMessage + System.lineSeparator());
        });
        String response = responseBuilder.toString();
        return new ResponseEntity<String>(response, HttpStatus.BAD_REQUEST);
    }

}
