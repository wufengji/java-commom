package com.wfj.common.validation.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author wfj
 * @since 2021/3/3
 */
public class EnumValidated implements ConstraintValidator<EnumValid, Integer> {
    private EnumValid enumValid;

    @Override
    public void initialize(EnumValid constraintAnnotation) {
        this.enumValid = constraintAnnotation;
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        if (enumValid.target() == null) {
            return false;
        }
        if (value == null) {
            return false;
        }
        for (Enum e : enumValid.target().getEnumConstants()) {
            if (e.toString().equals(String.valueOf(value))) {
                return true;
            }
        }
        return false;
    }
}
