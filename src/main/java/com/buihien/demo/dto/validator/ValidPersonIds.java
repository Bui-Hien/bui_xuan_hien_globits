package com.buihien.demo.dto.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PersonIdsValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPersonIds {
    String message() default "Person IDs must be greater than 0 if present";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
