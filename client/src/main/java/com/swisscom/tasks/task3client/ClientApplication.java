package com.swisscom.tasks.task3client;

import com.swisscom.tasks.task3client.service.JsonPlaceholderService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@SpringBootApplication
public class ClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientApplication.class, args);
	}
	@Bean
	JsonPlaceholderService jsonPlaceholderService() {
		RestClient client = RestClient.builder()
				.baseUrl("http://localhost:8080/api/v1")
				.build();
		HttpServiceProxyFactory factory = HttpServiceProxyFactory
				.builderFor(RestClientAdapter.create(client)).build();
		return factory.createClient(JsonPlaceholderService.class);
	}
}
