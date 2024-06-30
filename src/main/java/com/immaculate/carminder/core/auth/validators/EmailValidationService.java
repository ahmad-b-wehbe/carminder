package com.immaculate.carminder.core.auth.validators;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmailValidationService implements Predicate<String> {

    @Override
    public boolean test(String s) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }
}
