package com.pj.commons.utils;

import com.pj.commons.dto.Usuario;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Locale;

public class QuomBaseController {

    private final HttpServletRequest request;
    private final JwtUtil jwtUtil;

    public QuomBaseController(
            HttpServletRequest request,
            JwtUtil jwtUtil
    ){
        this.request = request;
        this.jwtUtil = jwtUtil;
    }

    protected Usuario getUser(){
        var authToken = request.getHeader("authToken");
        if(authToken == null)
            throw new RuntimeException("Unauthorized");
        return jwtUtil.getClaim(authToken.substring(7));
    }

    protected Locale getLocale(){
        var lng = request.getHeader("Accept-Language");
        if(lng == null){
            return Locale.getDefault();
        }
        return Locale.forLanguageTag(lng);
    }
}
