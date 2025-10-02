package com.pj.commons.exception;

import lombok.AllArgsConstructor;

import java.util.Locale;

@AllArgsConstructor
public class InvalidFieldException extends LocalizingException {

    private String field;

    public String getMessage(Locale locale){
        return String.format(Messages.getMessageForLocale("invalid-field", locale), field);
    }
}
