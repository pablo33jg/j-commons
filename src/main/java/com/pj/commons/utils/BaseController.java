package com.pj.commons.utils;

import com.pj.commons.dto.OmUser;
import com.pj.commons.dto.Usuario;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Locale;

public abstract class BaseController {

    private final HttpServletRequest request;
    private final OmJwtUtil jwtUtil;

    public BaseController(
            HttpServletRequest request,
            OmJwtUtil jwtUtil
    ){
        this.request = request;
        this.jwtUtil = jwtUtil;
    }

    protected OmUser getUser(){
        var authToken = request.getHeader("Authorization");
        if(authToken == null)
            throw new RuntimeException("Unauthorized");
        return jwtUtil.getAllClaim(authToken.substring(7));
    }

    protected Locale getLocale(){
        var lng = request.getHeader("Accept-Language");
        if(lng == null){
            return Locale.getDefault();
        }
        return Locale.forLanguageTag(lng);
    }
}
