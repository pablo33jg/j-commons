package com.pj.commons;

import com.pj.commons.utils.JwtUtil;
import com.pj.commons.utils.OmJwtUtil;
import com.pj.commons.utils.SqlMaker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonsAutoConfiguration {

    @Bean
    public SqlMaker sqlMaker(){
        return new SqlMaker();
    }

    @Bean
    public JwtUtil jwtUtil(){
        return new JwtUtil();
    }

    @Bean
    public OmJwtUtil omJwtUtil(){
        return new OmJwtUtil();
    }
}
