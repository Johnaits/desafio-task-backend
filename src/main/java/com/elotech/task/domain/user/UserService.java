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
                data.password(),
                data.role()
        );
        String pass = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(pass);
        return userRepository.save(newUser);
    }

    public User findById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public boolean existsByEmail(String email){
        return userRepository.findByEmail(email).isPresent();
    }
}
