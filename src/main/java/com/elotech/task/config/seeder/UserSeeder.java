package com.elotech.task.config.seeder;

import com.elotech.task.domain.user.User;
import com.elotech.task.domain.user.UserRolesEnum;
import com.elotech.task.domain.user.UserService;
import com.elotech.task.domain.user.dto.UserRequestDTO;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.seeder.enabled", havingValue = "true")
@Order(1)
public class UserSeeder implements CommandLineRunner {
    
    public static final String DEFAULT_EMAIL =  "testeApiApplication@gmail.com";

    private final UserService userService;

    public UserSeeder(
            UserService userService
    ){
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {

        if(this.userService.existsByEmail(DEFAULT_EMAIL)){
            String message = "Usuário com email: %s já existente. UserSeeder pulada";
            System.out.println(message.formatted(DEFAULT_EMAIL));
            return;
        }


        User user = this.userService.create(
                new UserRequestDTO("teste", DEFAULT_EMAIL, "123456", UserRolesEnum.ROLE_ADMIN)
        );
        String message = "Usuário %s criado!";
        System.out.println(message.formatted(DEFAULT_EMAIL));
    }
}
