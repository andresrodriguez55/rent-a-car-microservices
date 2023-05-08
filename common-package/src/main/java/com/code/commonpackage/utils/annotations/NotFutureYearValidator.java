package com.code.commonpackage.utils.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Year;

public class NotFutureYearValidator implements ConstraintValidator<NotFutureYear, Integer>
{
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        var currentYear = Year.now().getValue();
        return value <= currentYear;
    }
}
