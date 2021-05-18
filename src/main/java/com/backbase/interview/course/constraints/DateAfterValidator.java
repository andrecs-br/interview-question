package com.backbase.interview.course.constraints;

import java.time.LocalDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapperImpl;

public class DateAfterValidator
    implements ConstraintValidator<DateAfter, Object> {

    private String startDateProperty;
    private String endDateProperty;

    @Override
    public void initialize(DateAfter dateAfter) {
        startDateProperty = dateAfter.startDate();
        endDateProperty = dateAfter.endDate();
    }

    @Override
    public boolean isValid(Object value,
            ConstraintValidatorContext context) {

        LocalDate startDate = (LocalDate) new BeanWrapperImpl(value)
            .getPropertyValue(startDateProperty);
        LocalDate endDate = (LocalDate) new BeanWrapperImpl(value)
            .getPropertyValue(endDateProperty);

        return startDate != null && endDate != null && startDate.isBefore(endDate);
    }
}
