package com.buihien.demo.dto.validator;

import jakarta.validation.ConstraintValidator;

import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class PersonIdsValidator implements ConstraintValidator<ValidPersonIds, Set<Long>> {

    @Override
    public boolean isValid(Set<Long> personIds, ConstraintValidatorContext context) {
        if (personIds == null || personIds.isEmpty()) {
            return true;
        }
        return personIds.stream().allMatch(id -> id > 0);
    }
}