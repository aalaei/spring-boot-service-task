package com.swisscom.tasks.task3client.config;

import com.swisscom.tasks.task3client.dto.auth.LoginRequestDTO;
import com.swisscom.tasks.task3client.dto.auth.LoginResponseDTO;
import com.swisscom.tasks.task3client.service.JsonPaceHolderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class JsonPlaceholderServiceBean {
    private final Environment environment;

    @Bean
    JsonPaceHolderService jsonPlaceholderService() {
        String username = environment.getProperty("application.connection.security.auth.user", "");
        String password = environment.getProperty("application.connection.security.auth.pass", "");
        String url = environment.getProperty("application.connection.url", "http://localhost:8080");
        RestClient.Builder restBuilder = RestClient.builder().baseUrl(url + "/api/v1");
        if (!username.isEmpty()) {
            LoginRequestDTO loginRequestDTO = new LoginRequestDTO(username, password);
            String jwtToken = Objects.requireNonNull(RestClient.builder()
                    .baseUrl(url + "/api/v1/auth/login")
                    .build()
                    .post().body(loginRequestDTO)
                    .retrieve()
                    .body(LoginResponseDTO.class)).getJwt();
            restBuilder = restBuilder
                    .defaultHeader("Authorization", "Bearer " + jwtToken);
            log.info("Logged in successfully as " + username);
        }
        RestClient client = restBuilder.build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(client)).build();
        return factory.createClient(JsonPaceHolderService.class);
    }
}

