package org.edusync.tutor.config;

import org.edusync.tutor.security.CustomAccessDeniedHandler;
import org.edusync.tutor.security.CustomAuthenticationEntryPoint;
import org.edusync.tutor.security.JwtAuthenticationFilter;
import org.edusync.tutor.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public SecurityConfiguration(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable()) // REST API는 csrf 보안이 필요 없으므로 비활성화
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // JWT Token 인증방식으로 세션은 필요 없으므로 비활성화
                .authorizeHttpRequests(authorize -> authorize // 리퀘스트에 대한 사용권한 체크
                        .requestMatchers("/api/auth/register", "/api/auth/login", "/api/auth/exception").permitAll() // 가입 및 로그인 주소는 허용
                        .requestMatchers("**exception**").permitAll()
                        .anyRequest().hasRole("ADMIN")) // 나머지 요청은 인증된 ADMIN만 접근 가능
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(new CustomAccessDeniedHandler())
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint()))
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class); // JWT Token 필터를 id/password 인증 필터 이전에 추가

        return httpSecurity.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/v3/api-docs/**", "/swagger-resources/**", "/swagger-ui/**", "/webjars/**", "/swagger/**","/sign-api/exception");
    }
}