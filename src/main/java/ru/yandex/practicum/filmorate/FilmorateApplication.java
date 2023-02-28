package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;

@SuppressWarnings("SpellCheckingInspection")
@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class FilmorateApplication {

	public static void main(String[] args) {
		SpringApplication.run(FilmorateApplication.class, args);
	}
	@Autowired
	RequestMappingHandlerMapping requestMappingHandlerMapping;

	@PostConstruct
	public void printEnpoints() {
		requestMappingHandlerMapping.getHandlerMethods().forEach((k,v) -> log.info(String.format("Mapped %s -> %s", k, v)));
	}
}
