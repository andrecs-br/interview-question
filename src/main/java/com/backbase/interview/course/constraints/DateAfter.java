package com.backbase.interview.course.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = DateAfterValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface DateAfter {

    String message() default "End date needs to be after the start date";

    String startDate();

    String endDate();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
