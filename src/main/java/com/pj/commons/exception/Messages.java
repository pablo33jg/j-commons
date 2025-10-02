package com.pj.commons.exception;

import java.util.Locale;
import java.util.ResourceBundle;

public class Messages {

    public static String getMessageForLocale(String messageKey, Locale locale){
        return ResourceBundle.getBundle("messages", locale).getString(messageKey);
    }

    public static String getFieldName(String field, Locale locale){
        return ResourceBundle.getBundle("fields", locale).getString(field);
    }
}
