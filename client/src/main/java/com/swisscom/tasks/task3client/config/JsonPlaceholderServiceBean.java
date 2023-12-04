package com.swisscom.tasks.task3client.config;

import com.swisscom.tasks.task3client.dto.auth.LoginRequestDTO;
import com.swisscom.tasks.task3client.dto.auth.LoginResponseDTO;
import com.swisscom.tasks.task3client.service.JsonPlaceholderService;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Objects;
// This Bean is disabled because we are using FeignClient
//@Component
public class JsonPlaceholderServiceBean {
//    @Bean
    JsonPlaceholderService jsonPlaceholderService() {
		RestClient client = RestClient.builder()
				.baseUrl("http://localhost:8080/api/v1/auth")
				.build();
		LoginRequestDTO loginRequestDTO=new LoginRequestDTO("admin", "admin");
		String jwtToken= Objects.requireNonNull(client.post()
				.uri("/login")
				.body(loginRequestDTO)
				.retrieve()
				.body(LoginResponseDTO.class)).getJwt();
		RestClient client2 = RestClient.builder()
				.baseUrl("http://localhost:8080/api/v1")
				.defaultHeader("Authorization", "Bearer "+jwtToken)
				.build();
		HttpServiceProxyFactory factory = HttpServiceProxyFactory
				.builderFor(RestClientAdapter.create(client2)).build();
		return factory.createClient(JsonPlaceholderService.class);
	}
}

