package com.elotech.task.domain.user;

import com.elotech.task.domain.user.dto.UserRequestDTO;
import com.elotech.task.domain.user.dto.UserResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@RequestBody @Valid UserRequestDTO user){
        User newUser = userService.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponseDTO(newUser));

    }
}
