package com.project.NovelWeb.exceptions;

import com.project.NovelWeb.responses.ResponseObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestControllerAdvice //xử lý ngoại lệ chung
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ResponseObject> handleGeneralException(Exception exception) {
        return ResponseEntity.internalServerError().body(
                ResponseObject.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .message(exception.getMessage())
                        .build()
        );
    }
    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleResourceNotFoundException(DataNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseObject.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(exception.getMessage())
                .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseObject> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<String> errors = bindingResult.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        return ResponseEntity.badRequest().body(ResponseObject.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(String.join(", ", errors))
                .build());
    }

    @ExceptionHandler(MaximumMemoryExceededException.class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    public ResponseObject handleMaximumMemoryExceededException(Exception e) {
        return new ResponseObject(
                "IMAGE FILES TOO LARGE",
                HttpStatus.PAYLOAD_TOO_LARGE,
                e.getMessage()
        );
    }

    @ExceptionHandler(UnsupportedMediaTypeException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ResponseObject handleUnsupportedMediaTypeException(Exception e) {
        return new ResponseObject(
                "UNSUPPORTED MEDIA TYPE",
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                e.getMessage()
        );
    }

    @ExceptionHandler(ExpiredTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseObject handleExpiredTokenException(Exception e) {
        return new ResponseObject(
                "EXPIRED TOKEN",
                HttpStatus.UNAUTHORIZED,
                e.getMessage()
        );
    }
}