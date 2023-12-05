package com.swisscom.tasks.task3.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.annotations.servers.Server;

/**
 * OpenApiConfig is the configuration class for OpenApi.
 */
@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Ali Alaei",
                        email = "ali.alaei.tabatabaei@gamil.com",
                        url = "https://alaei.me"
                ),
                description = "OpenApi Documentation for Service Management App",
                title = "OpenApi Specification",
                version = "1.0",
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                ),
                termsOfService = "Terms of Service"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "Prod ENV",
                        url = "http://localhost:3000"
                )
        },
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)
@SecuritySchemes(
        {
                @SecurityScheme(
                        name = "bearerAuth",
                        description = "JWT auth description",
                        scheme = "bearer",
                        type = SecuritySchemeType.HTTP,
                        bearerFormat = "JWT",
                        in = SecuritySchemeIn.HEADER
                )
        }
)
public class OpenApiConfig {
}
