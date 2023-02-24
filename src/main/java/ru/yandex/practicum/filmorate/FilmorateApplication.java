package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;

@SuppressWarnings("SpellCheckingInspection")
@SpringBootApplication
@RequiredArgsConstructor
public class FilmorateApplication {

	public static void main(String[] args) {
		SpringApplication.run(FilmorateApplication.class, args);
	}
	@Autowired
	RequestMappingHandlerMapping requestMappingHandlerMapping;

	@PostConstruct
	public void printEnpoints() {
		requestMappingHandlerMapping.getHandlerMethods().forEach((k,v) -> System.out.println(k + " -> "+ v));
	}
}
