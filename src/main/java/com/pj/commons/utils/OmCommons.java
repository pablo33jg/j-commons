package com.pj.commons.utils;

import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OmCommons {

    private static Logger log = LoggerFactory.getLogger(OmCommons.class);

    public enum PLATFORM_ORIGIN{
        TIMEST,
        BOOK
    }

    public static PLATFORM_ORIGIN getOrigin(Claims claims){
        try {
            return PLATFORM_ORIGIN.valueOf(claims.get("origin", String.class));
        }catch (IllegalArgumentException e){
            log.error("No se reconoce el origen del token; error: {}", e.getMessage(), e.getCause());
            throw new RuntimeException("Token origin is unknown");
        }
    }
}
