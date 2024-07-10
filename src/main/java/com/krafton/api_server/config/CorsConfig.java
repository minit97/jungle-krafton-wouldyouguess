package com.krafton.api_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); // URL 기반의 CORS 구성을 제공하는 클래스
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);       // 요청에서 인증 정보 (예: 쿠키)를 허용하도록 설정
        config.addAllowedOriginPattern("*");    // 특정 도메인을 지정하여 보안을 강화
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
