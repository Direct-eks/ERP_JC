package org.jc.backend.config.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DecimalValidator.class)
@Target(value = { ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
public @interface DecimalValidation {

    enum ValidationTypeEnum {
        INTEGER, DECIMAL_2, DECIMAL_8
    }

    ValidationTypeEnum type();
    String message();

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
