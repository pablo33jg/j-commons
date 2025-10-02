package com.pj.commons.exception;

import java.util.Locale;

public abstract class LocalizingException extends RuntimeException{

    public String getMessage(Locale locale){
        return "";
    }
}
