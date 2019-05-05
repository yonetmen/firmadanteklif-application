package com.firmadanteklif.application.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.lang.annotation.*;

@Size.List({
        @Size(min = 5, message = "{email.size.min.message}"),
        @Size(max = 50, message = "{email.size.max.message}")
})
@Email(message = "{email.format.message}")
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailValidator.class)
public @interface ValidEmail {

    String message();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
