package com.rushional.cities.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@NotNull(message = "A password is required")
@Size(min = 8, max = 20, message = "Password must be 8-20 characters long")
@Pattern(regexp = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[!?.\\_\\-=$&@])[a-zA-Z0-9!?.\\_\\-=$&@]+$",
    message = "Password must contain at least one number, one uppercase letter, " +
        "and one special symbol: !?. _-=$&@")
public @interface CustomPassword {

  String message() default "Invalid password";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
