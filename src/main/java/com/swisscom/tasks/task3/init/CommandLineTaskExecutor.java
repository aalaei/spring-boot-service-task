package com.swisscom.tasks.task3.init;

import com.swisscom.tasks.task3.model.auth.LoginRequest;
import com.swisscom.tasks.task3.model.auth.LoginResponseDTO;
import com.swisscom.tasks.task3.model.auth.Role;
import com.swisscom.tasks.task3.model.auth.User;
import com.swisscom.tasks.task3.repository.RoleRepository;
import com.swisscom.tasks.task3.repository.UserRepository;
import com.swisscom.tasks.task3.service.AuthenticationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
@Slf4j
@Component
@AllArgsConstructor
public class CommandLineTaskExecutor implements CommandLineRunner {
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final Environment environment;
    @Override
    public void run(String... args) throws Exception {
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
    }
}
