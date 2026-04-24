package com.elotech.task.domain.user;

import com.elotech.task.domain.user.dto.UserRequestDTO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User create(UserRequestDTO data){
        User newUser = new User(
                data.name(),
                data.email(),
                data.password()
        );
        return this.save(newUser);

    }

    public User save(User user){
        String pass = passwordEncoder.encode(user.getPassword());
        user.setPassword(pass);
        return userRepository.save(user);
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }
}
