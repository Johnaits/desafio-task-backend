package com.elotech.task.domain.auth;

import com.elotech.task.config.security.TokenService;
import com.elotech.task.domain.auth.dto.AuthenticationDTO;
import com.elotech.task.domain.auth.dto.TokenResponseDTO;
import com.elotech.task.domain.user.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService){
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data){
        var authenticationToken = new UsernamePasswordAuthenticationToken(data.email(), data.password());

        var auth = authenticationManager.authenticate(authenticationToken);

        User loggedUser = (User) auth.getPrincipal();

        String tokenJWT = tokenService.generateToken(loggedUser);

        Long expiresInSeconds = this.tokenService.getTimeExpiration() * 3600;

        return ResponseEntity.ok(new TokenResponseDTO(tokenJWT, "Bearer", expiresInSeconds));

    }
}
