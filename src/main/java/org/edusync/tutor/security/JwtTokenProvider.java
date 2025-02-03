package org.edusync.tutor.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    private final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);
    private final UserDetailsService userDetailsService;
    private Key secretKey;

    @Autowired
    public JwtTokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    protected void init() {
        // secretKey 초기화
        LOGGER.info("[init] JwtTokenProvider secretKey 초기화");
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        LOGGER.info("[init] JwtTokenProvider secretKey 초기화 완료");
    }

    public String createToken(String userUid, List<String> roles) {
        // 토큰 생성
        LOGGER.info("[createToken] 토큰 생성 시작");
        Claims claims = Jwts.claims().setSubject(userUid);
        claims.put("roles", roles);

        Date now = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + 1000L * 60 * 60)) // 1시간 유효기간
                .signWith(secretKey)
                .compact();

        LOGGER.info("[createToken] 토큰 생성 완료");
        return token;
    }

    public Authentication getAuthentication(String token) {
        // 토큰에서 인증 정보 가져오기
        LOGGER.info("[getAuthentication] 토큰에서 인증 정보 가져오기");
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUsername(token));
        LOGGER.info("[getAuthentication] 인증 정보 가져오기 완료, UserDetails UserName: {}", userDetails.getUsername());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        // 토큰에서 사용자 이름 추출
        LOGGER.info("[getUsername] 토큰에서 사용자 이름 추출");
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        // HTTP 헤더에서 토큰 추출
        LOGGER.info("[resolveToken] HTTP 헤더에서 토큰 추출");
        return request.getHeader("X-AUTH-TOKEN");
    }

    public boolean validateToken(String token) {
        // 토큰 유효성 검증
        LOGGER.info("[validateToken] 토큰 유효성 검증 시작");
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            LOGGER.info("[validateToken] 토큰 유효성 검증 성공");
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            LOGGER.info("[validateToken] 토큰 유효성 검증 실패");
            return false;
        }
    }
}