package it.danilo.blog.config;



import it.danilo.blog.presentationlayer.api.exceptions.ApiValidationException;
import it.danilo.blog.presentationlayer.api.exceptions.NotFoundException;
import it.danilo.blog.presentationlayer.api.exceptions.PasswordNotCorrectException;
import it.danilo.blog.presentationlayer.api.exceptions.duplicated.DuplicateKeyException;
import it.danilo.blog.presentationlayer.api.exceptions.user.UserException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    public record ExceptionInfo (
            String message,
            String key) {}

    public record ExceptionValidationInfo (
            String field,
            String contentOfError
            ){}

    public record ExceptionUserInfo (
            String username,
            String password,
            String message
            ){}


    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<?> handleRuntimeException(RuntimeException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    protected ResponseEntity<?> handleDuplicatedKey(DuplicateKeyException e) {
        return new ResponseEntity<>(new ExceptionInfo(e.getMessage(), e.key), e.status);
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<?> handleNotFoundedElement(NotFoundException e) {
        return new ResponseEntity<>(new ExceptionInfo(e.getMessage(), String.valueOf(e.idNotFound)), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ApiValidationException.class)
    protected ResponseEntity<?> handleApiValidationException(ApiValidationException e) {
        var body = e.errorsList.stream() //
                .filter(error -> error instanceof FieldError)//
                .map(error -> (FieldError) error) //
                .map(error -> new ExceptionValidationInfo(error.getField(), error.getDefaultMessage())//
                ).toList();

        return new ResponseEntity<>(body, e.status);
    }


    @ExceptionHandler(UserException.class)
    protected ResponseEntity<?> handleUserException(UserException e) {
        return new ResponseEntity<>(new ExceptionUserInfo(e.email, e.password, e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(PasswordNotCorrectException.class)
    protected ResponseEntity<?> PasswordNotCorrectException(PasswordNotCorrectException e) {
        return new ResponseEntity<>(new ExceptionInfo(e.getMessage(), e.password), HttpStatus.NOT_ACCEPTABLE);
    }

}
