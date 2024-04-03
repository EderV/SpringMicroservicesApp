package com.evm.ms.auth.application.base;

import java.util.List;
import java.util.regex.Pattern;

public abstract class BaseTextChecker {

    private final String ILLEGAL_CHARACTERS_PATTERN = "[<>\"';]";

    public void nullCheck(Object object) throws IllegalArgumentException {
        if (object == null) {
            throw new IllegalArgumentException("Input data is null");
        }
    }

    public void checkIllegalCharacters(String text) throws IllegalArgumentException {
        if (findIllegalCharacters(text)) {
            throw new IllegalArgumentException("Data provided contains illegal characters");
        }
    }

    public void checkIllegalCharacters(List<String> texts) throws IllegalArgumentException {
        texts.forEach(this::checkIllegalCharacters);
    }

    private boolean findIllegalCharacters(String input) {
        if (input != null) {
            var regex = Pattern.compile(ILLEGAL_CHARACTERS_PATTERN);
            var matcher = regex.matcher(input);
            return matcher.find();
        }
        return false;
    }

}
