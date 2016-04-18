package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.OAuth2AuthorizationServerConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@EnableAuthorizationServer
@SpringBootApplication
public class OAuth2Application {

	public static void main(String[] args) {
		SpringApplication.run(OAuth2Application.class, args);
	}
}

@Configuration
class JwtOAuth2AuthorizationServerConfiguration extends OAuth2AuthorizationServerConfiguration {

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints)
			throws Exception {
		super.configure(endpoints);
		endpoints.accessTokenConverter(jwtTokenEnhancer());
	}

	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(jwtTokenEnhancer());
	}

	@Bean
	public JwtAccessTokenConverter jwtTokenEnhancer() {
		return new JwtAccessTokenConverter();
	}

}
@Configuration
class CustomOAuth2AuthorizationServerConfiguration extends OAuth2AuthorizationServerConfiguration {

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints)
			throws Exception {
		super.configure(endpoints);
		endpoints.tokenServices(new DefaultTokenServices());
	}

	@Bean
	public DefaultTokenServices defaultTokenServices() {
		return new DefaultTokenServices() {
			@Override
			public OAuth2Authentication loadAuthentication(String accessTokenValue) throws AuthenticationException,
					InvalidTokenException {
				return super.loadAuthentication(accessTokenValue);
			}
		};
	}

}