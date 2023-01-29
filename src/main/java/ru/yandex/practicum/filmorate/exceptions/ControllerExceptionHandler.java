package ru.yandex.practicum.filmorate.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import ru.yandex.practicum.filmorate.exceptions.KeyNotFoundException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = {KeyNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, Object>> validationException(KeyNotFoundException ex, ServletWebRequest request) {
        log.warn(ex.getMessage());

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

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
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
        bodyMap.put("dataclass", ex.getObjectName());
        bodyMap.put("error", ex.getMessage());
        bodyMap.put("errorlist", errorList);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(bodyMap);
    }

}
