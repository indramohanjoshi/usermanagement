package com.usermanagement.validator;

import org.passay.*;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.usermanagement.user.dto.LoginRequest;
import com.usermanagement.user.dto.UserDto;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class UserFormValidator implements Validator {

    private static final Set<Class> supportedBeans = new HashSet<>();

    static {
        supportedBeans.add(UserDto.class);
        supportedBeans.add(LoginRequest.class);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
        //return supportedBeans.contains(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target instanceof LoginRequest) {
            //validateLoginRequest(target, errors);
        } else if (target instanceof UserDto) {
            //validateUserDto(target, errors);
        }
    }

    private void validateLoginRequest(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "invalid.username", "Invalid value for field username.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "invalid.password", "Invalid value for field password.");
    }

    private void validateUserDto(Object target, Errors errors) {
        UserDto userDto = (UserDto) target;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "invalid.firstName", "Invalid value for field firstName.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "invalid.lastName", "Invalid value for field lastName.");
        String password = userDto.getPassword();
        boolean valid = validatePassword(password);
        if (valid && !password.equals(userDto.getMatchingPassword())) {
            errors.rejectValue("password", "not.match.password", "password does not match with matchingPassword.");
        }
        ValidationUtils.rejectIfEmpty(errors, "roles", "invalid.roles", "Invalid value for field roles.");
    }

    private boolean validatePassword(String password) {
        PasswordValidator validator = new PasswordValidator(Arrays.asList(
                new LengthRule(8, 30),
                new UppercaseCharacterRule(1),
                new DigitCharacterRule(1),
                new SpecialCharacterRule(1),
                new NumericalSequenceRule(3, false),
                new AlphabeticalSequenceRule(3, false),
                new QwertySequenceRule(3, false),
                new WhitespaceRule()));

        return validator.validate(new PasswordData(password)).isValid();
    }
}
