package org.jc.backend.config.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DecimalValidator implements ConstraintValidator<DecimalValidation, String> {

    // 0, or valid number beginning with 1-9
    private final static Pattern integerPattern =
            Pattern.compile("^((0)|[1-9]\\d*)$");
    private final static Pattern decimal2Pattern =
            Pattern.compile("^((0)|([1-9]\\d*)|([1-9][\\d]*?\\.\\d{1,2})|(0\\.\\d{1,2}))$");
    private final static Pattern decimal8Pattern =
            Pattern.compile("^((0)|([1-9]\\d*)|([1-9][\\d]*?\\.\\d{1,8})|(0\\.\\d{1,8}))$");

    private DecimalValidation.ValidationTypeEnum type;

    @Override
    public void initialize(DecimalValidation constraintAnnotation) {
        type = constraintAnnotation.type();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Matcher matcher;
        switch (type) {
            case INTEGER:
                matcher = integerPattern.matcher(value);
                break;
            case DECIMAL_2:
                matcher = decimal2Pattern.matcher(value);
                break;
            case DECIMAL_8:
                matcher = decimal8Pattern.matcher(value);
                break;
            default:
                return false;
        }
        return matcher.matches();
    }
}
