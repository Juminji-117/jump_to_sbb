package com.ll.exam.sbb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

// 시큐리티 설정
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // 모든 경로
                .antMatchers("/**")
                // 허용 한다.
                .permitAll()
                .and() // 문맥의 끝
                .csrf().ignoringAntMatchers("/h2-console/**")
                .and()
                .headers().addHeaderWriter(new XFrameOptionsHeaderWriter(
                        XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
                .and()
                .formLogin() // 양식 로그인 위해 구성 확장 // Spring Security에서 제공하는 기본값 재정의
                .loginPage("/user/login") // 사용자 정의 로그인 페이지
                .defaultSuccessUrl("/"); // 성공적인 로그인 후 랜딩 페이지

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
