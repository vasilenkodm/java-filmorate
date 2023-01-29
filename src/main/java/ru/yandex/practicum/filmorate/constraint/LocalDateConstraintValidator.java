package ru.yandex.practicum.filmorate.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class LocalDateConstraintValidator implements ConstraintValidator<LocalDateConstraint, LocalDate> {
    private LocalDate minDate;
    private LocalDate maxDate;

    @Override
    public void initialize(LocalDateConstraint constraint) {
        String parmMinDate = constraint.minDate();
        String parmMaxDate = constraint.maxDate();
        if (!parmMinDate.isBlank()) {
            String[] parts = parmMinDate.split("\\D");
            if (parts.length==3) {
                minDate = LocalDate.of(Integer.valueOf(parts[0]), Integer.valueOf(parts[1]), Integer.valueOf(parts[2]));
            }
        }
        if (!parmMaxDate.isBlank()) {
            String[] parts = parmMaxDate.split("\\D");
            if (parts.length==3) {
                maxDate = LocalDate.of(Integer.valueOf(parts[0]), Integer.valueOf(parts[1]), Integer.valueOf(parts[2]));
            }
        }
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if (date==null) return false;
        if (minDate!=null && minDate.compareTo(date)>0) return false;
        return maxDate == null || maxDate.compareTo(date) >= 0;
    }
}
