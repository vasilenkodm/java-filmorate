package ru.yandex.practicum.filmorate.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LocalDateConstraintValidator.class)
public @interface LocalDateConstraint {
    String minDate() default "";
    String maxDate() default "";
    String message() default "Дата вне диапазона!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
