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

    public enum GENERAL_STATUS {
        NOT_ASSIGNED,
        ACTIVE,
        DISABLED;

        public static GENERAL_STATUS fromValue(String value) {
            for (GENERAL_STATUS type : GENERAL_STATUS.values()) {
                if (type.name().equalsIgnoreCase(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("No se encontró ningún tipo: " + value);
        }
    }

    public enum STORES_TYPE {
        NOT_ASSIGNED,
        STORE,
        SUPPLIES,
        INTERNAL_CONSIGNMENT,
        EXTERNAL_CONSIGNMENT,
        TRANSFORMATION;

        public static STORES_TYPE fromValue(String value){
            for(STORES_TYPE type: STORES_TYPE.values()){
                if(type.name().equalsIgnoreCase(value)){
                    return type;
                }
            }
            throw new IllegalArgumentException("No se encontró el tipo de almacén" + value);
        }
    }

    public static final Integer SCOPE_ADMIN = 1;
    public static final Integer SCOPE_BRANCH = 2;
    public static final Integer SCOPE_OWN = 3;
}
