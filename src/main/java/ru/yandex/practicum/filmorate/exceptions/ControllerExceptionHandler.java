package ru.yandex.practicum.filmorate.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {
    @SuppressWarnings("unused")
    @ExceptionHandler(value = {KeyNotFoundException.class})
    public ResponseEntity<Map<String, Object>> exceptionHandler(KeyNotFoundException ex, ServletWebRequest request) {
        Map<String, Object> bodyMap = new LinkedHashMap<>();
        bodyMap.put("timestamp", Instant.now());
        bodyMap.put("status", HttpStatus.NOT_FOUND);
        bodyMap.put("path", request.getRequest().getRequestURI());
        bodyMap.put("dataclass", ex.getKeyOwnerClass().getSimpleName());
        bodyMap.put("error", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(bodyMap);
    }

    @SuppressWarnings("unused")
    @ExceptionHandler(value = {FeatureNotSupportedException.class})
    public ResponseEntity<Map<String, Object>> exceptionHandler(FeatureNotSupportedException ex, ServletWebRequest request) {
        Map<String, Object> bodyMap = new LinkedHashMap<>();
        bodyMap.put("timestamp", Instant.now());
        bodyMap.put("status", HttpStatus.NOT_IMPLEMENTED);
        bodyMap.put("path", request.getRequest().getRequestURI());
        bodyMap.put("dataclass", ex.getKeyOwnerClass().getSimpleName());
        bodyMap.put("error", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_IMPLEMENTED)
                .body(bodyMap);
    }

    @SuppressWarnings("unused")
    @ExceptionHandler(value = {UnexpectedErrorException.class})
    public ResponseEntity<Map<String, Object>> exceptionHandler(UnexpectedErrorException ex, ServletWebRequest request) {
        Map<String, Object> bodyMap = new LinkedHashMap<>();
        bodyMap.put("timestamp", Instant.now());
        bodyMap.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
        bodyMap.put("path", request.getRequest().getRequestURI());
        bodyMap.put("dataclass", ex.getKeyOwnerClass().getSimpleName());
        bodyMap.put("error", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(bodyMap);
    }

    @SuppressWarnings("unused")
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, Object>> methodArgumentNotValidException(MethodArgumentNotValidException ex, ServletWebRequest request) {
        log.warn(ex.getMessage());

        List<String> errorList=  new ArrayList<>(ex.getAllErrors().size());
        for (ObjectError error: ex.getAllErrors()) {
            errorList.add(error.getObjectName()+"."+error.getCode()+":" +error.getDefaultMessage());
        }

        Map<String, Object> bodyMap = new LinkedHashMap<>();
        bodyMap.put("timestamp", Instant.now());
        bodyMap.put("status", HttpStatus.BAD_REQUEST);
        bodyMap.put("path", request.getRequest().getRequestURI());
        bodyMap.put("dataClass", ex.getObjectName());
        bodyMap.put("error", ex.getMessage());
        bodyMap.put("errorList", errorList);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(bodyMap);
    }
}
