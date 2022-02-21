package com.frankmoley.security.app;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import nz.net.ultraq.thymeleaf.decorators.strategies.GroupingStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.List;

@SpringBootApplication
@EnableOAuth2Client
public class GuestAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuestAppApplication.class, args);
	}

	private static final String AUTH_TOKEN_URL = "/oauth/token";
	private static final String AUTHORIZE_URL = "/oauth/authorize";

	@Value("${landon.guest.service.url}")
	private String serviceUrl;

	@Bean
	public OAuth2RestTemplate restTemplate() {
		ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();
		resource.setAccessTokenUri(serviceUrl + AUTH_TOKEN_URL);
		resource.setClientId("guest_app");
		resource.setClientSecret("secret");
		resource.setGrantType("client_credentials");
		resource.setScope(List.of("READ_ALL_GUESTS", "WRITE_GUEST", "UPDATE_GUEST"));
		resource.setAuthenticationScheme(AuthenticationScheme.form);
		AccessTokenRequest request = new DefaultAccessTokenRequest();
		return new OAuth2RestTemplate(resource, new DefaultOAuth2ClientContext(request));
	}


	@Bean
	public SpringTemplateEngine templateEngine(ITemplateResolver templateResolver) {
		final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);
		templateEngine.addDialect(new SpringSecurityDialect()); // Enable use of "sec"
		templateEngine.addDialect(new LayoutDialect(new GroupingStrategy()));
		return templateEngine;
	}

}
