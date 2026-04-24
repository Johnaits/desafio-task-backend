package com.elotech.task.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.elotech.task.domain.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    private static final String ISSUER = "task-api";

    private final long timeExpiration;

    private final Algorithm algorithm;

    public TokenService(
            @Value("${api.security.token.secret}") String secret,
            @Value("${api.security.token.expiration}") long timeExpiration
    ){
        this.algorithm = Algorithm.HMAC256(secret);
        this.timeExpiration = timeExpiration;
    }

    public String generateToken(User user){
        try{
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(user.getEmail())
                    .withExpiresAt(genExpirationDate())
                    .sign(this.algorithm);

        } catch(JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar o token JWT", exception);
        }
    }

    public String validateToken(String token){
        try{
            return JWT.require(this.algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (com.auth0.jwt.exceptions.JWTVerificationException exception) {
            return"";
        }
    }

    private Instant genExpirationDate(){
        return LocalDateTime.now()
                .plusHours(this.getTimeExpiration())
                .toInstant(ZoneOffset.of("-03:00"));
    }

    public Long getTimeExpiration() {
        return timeExpiration;
    }
}
