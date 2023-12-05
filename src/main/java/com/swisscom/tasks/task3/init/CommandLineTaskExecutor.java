package com.swisscom.tasks.task3.init;

import com.swisscom.tasks.task3.dto.auth.LoginRequestDTO;
import com.swisscom.tasks.task3.dto.auth.LoginResponseDTO;
import com.swisscom.tasks.task3.model.auth.Role;
import com.swisscom.tasks.task3.model.auth.User;
import com.swisscom.tasks.task3.repository.RoleRepository;
import com.swisscom.tasks.task3.repository.UserRepository;
import com.swisscom.tasks.task3.service.AuthenticationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * This class is used to execute some code after the application has started.
 * It creates some users and roles in the database.
 * In the development environment it also prints the url to the GraphQL console.
 */
@Slf4j
@Component
@AllArgsConstructor
public class CommandLineTaskExecutor implements CommandLineRunner {
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final Environment environment;

    private void createRoleIfNotExists(Role.RoleType roleType) {
        if (roleRepository.findByAuthority(roleType.name()).isEmpty()) {
            Role role = new Role(roleType);
            roleRepository.save(role);
        }
    }

    @Override
    public void run(String... args) throws Exception {
        String password = environment.getProperty("admin-pass", "admin");
        // Create Roles If not exists in the database.
        List.of(Role.RoleType.values()).forEach(this::createRoleIfNotExists);
        if (!userRepository.existsByUsername("admin")) {
            List<Role> adminRoles = roleRepository.findAll();
            userRepository.save(new User("admin", passwordEncoder.encode(password), adminRoles));
        }
        if (Arrays.stream(environment.getActiveProfiles()).anyMatch(env -> env.contains("dev"))) {
            LoginResponseDTO loginResponseDTO = authenticationService.loginUser(
                    new LoginRequestDTO("admin", password)
            );
            String graphQLConsole = "http://localhost:8080/graphiql#Authorization=Bearer%20" + loginResponseDTO.getJwt();
            log.info("GraphQLConsole: {}", graphQLConsole);
        }
    }
}
