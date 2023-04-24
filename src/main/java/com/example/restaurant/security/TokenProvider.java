package com.example.restaurant.security;

import com.example.restaurant.domain.type.UserType;
import com.example.restaurant.domain.vo.MemberVo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;


public class TokenProvider {
    private final String KEY_ROLES = "roles";
    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24;

    @Value("${spring.jwt.secret}")
    private String secretKey;


    public String createToken(Long memberId, String memberEmail, UserType memberFlag) {
        Claims claims = Jwts.claims().setId(memberId.toString()).setSubject(memberEmail);
        claims.put(KEY_ROLES, memberFlag);

        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + TOKEN_EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }


    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }


    public boolean validateToken(String token) {
        Claims claims = parseClaims(token);
        return !claims.getExpiration().before(new Date());
    }


    public MemberVo getMemberVo(String token) {
        Claims claims = parseClaims(token);
        return new MemberVo(Long.valueOf(claims.getId()), claims.getSubject());
    }
}
