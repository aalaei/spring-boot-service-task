package com.swisscom.tasks.task3;

import com.swisscom.tasks.task3.model.auth.LoginRequest;
import com.swisscom.tasks.task3.model.auth.LoginResponseDTO;
import com.swisscom.tasks.task3.model.auth.User;
import com.swisscom.tasks.task3.model.auth.Role;
import com.swisscom.tasks.task3.repository.RoleRepository;
import com.swisscom.tasks.task3.repository.UserRepository;
import com.swisscom.tasks.task3.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

/**
 * This is the main class of the application.
 * It is used to start the application.
 */
@EnableAsync
@SpringBootApplication
@EnableCaching
@Slf4j
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    @Bean
    CommandLineRunner run(RoleRepository roleRepository, UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          AuthenticationService authenticationService,
                          Environment environment
    ){
        return args -> {
            if(userRepository.findByUsername("admin").isEmpty()) {
                Role userRole = new Role(Role.RoleType.USER);
                Role adminRole = new Role(Role.RoleType.ADMIN);
                Role superUser = new Role(Role.RoleType.SUPER_USER);
                roleRepository.save(userRole);
                roleRepository.save(adminRole);
                roleRepository.save(superUser);

                userRepository.save(
                        new User("user", passwordEncoder.encode("user"), List.of(userRole))
                );

                userRepository.save(
                        new User("super", passwordEncoder.encode("super"),
                                List.of(userRole, superUser))
                );

                userRepository.save(
                        new User("admin", passwordEncoder.encode("admin"),
                                List.of(adminRole, userRole, superUser)
                        )
                );
            }
            if(Arrays.stream(environment.getActiveProfiles()).anyMatch(env-> env.contains("dev"))) {
                LoginResponseDTO loginResponseDTO = authenticationService.loginUser(
                        new LoginRequest("admin", "admin")
                );
                String graphQLConsole = "http://localhost:8080/graphiql#Authorization=Bearer%20" + loginResponseDTO.getJwt();
                log.info("GraphQLConsole: {}", graphQLConsole);
            }
        };
    }
}
