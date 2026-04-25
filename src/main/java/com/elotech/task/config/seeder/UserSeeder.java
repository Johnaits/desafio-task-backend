package com.elotech.task.config.seeder;

import com.elotech.task.domain.user.User;
import com.elotech.task.domain.user.UserService;
import com.elotech.task.domain.user.dto.UserRequestDTO;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserSeeder implements CommandLineRunner {

    private final UserService userService;

    public UserSeeder(UserService userService){
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        String defaultEmail = "testeApiApplication@gmail.com";

        if(this.userService.existsByEmail(defaultEmail)){
            String message = "Usuário com email: %s já existente. UserSeeder pulada";
            System.out.println(message.formatted(defaultEmail));
            return;
        }


        User user = this.userService.create(
                new UserRequestDTO("teste", defaultEmail, "123456")
        );
        String message = "Usuário %s criado!";
        System.out.println(message.formatted(defaultEmail));
    }
}
