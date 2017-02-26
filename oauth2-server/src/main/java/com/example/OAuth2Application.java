package com.example;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.AuthorizationServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.authserver.OAuth2AuthorizationServerConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;

@EnableAuthorizationServer
@SpringBootApplication
public class OAuth2Application {

    public static void main(String[] args) {
        SpringApplication.run(OAuth2Application.class, args);
    }
}
@Configuration
class UserAuthenticationConfigurerAdapter extends GlobalAuthenticationConfigurerAdapter {
    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("chulsoo").password("1111").roles("USER").and()
            .withUser("jungin11").password("1111").roles("USER").and()
            .withUser("jwryu991").password("1111").roles("USER");
    }
}
@Configuration
class OAuth2Configuration {

    @Bean
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(
            ResourceServerProperties resourceServerProperties) {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        accessTokenConverter.setSigningKey(resourceServerProperties.getJwt().getKeyValue());
        return accessTokenConverter;
    }

    @Bean
    @Primary
    public JdbcClientDetailsService jdbcClientDetailsService(DataSource dataSource) {
        return new JdbcClientDetailsService(dataSource);
    }

}

@Configuration
class JwtOAuth2AuthorizationServerConfiguration extends OAuth2AuthorizationServerConfiguration {

    private final ClientDetailsService clientDetailsService;

    public JwtOAuth2AuthorizationServerConfiguration(BaseClientDetails details,
                                                     AuthenticationManager authenticationManager,
                                                     ObjectProvider<TokenStore> tokenStoreProvider,
                                                     ObjectProvider<AccessTokenConverter> tokenConverter,
                                                     AuthorizationServerProperties properties,
                                                     ClientDetailsService clientDetailsService) {
        super(details, authenticationManager, tokenStoreProvider, tokenConverter, properties);
        this.clientDetailsService = clientDetailsService;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints)
            throws Exception {
        super.configure(endpoints);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
/*
        // 이부분 주석을 풀고 위의 코드를 주석처리하면
        // 클라이언트 정보를 직접 기술 할 수 있다
        // @formatter:off
        clients.inMemory()
                // 클라이언트 아이디
            .withClient("my_client_id")
                // 클라이언트 시크릿
                .secret("my_client_secret")
                // 엑세스토큰 발급 가능한 인증 타입
                // 기본이 다섯개, 여기 속성이 없으면 인증 불가
                .authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit", "refresh_token")
                // 클라이언트에 부여된 권한
                .authorities("ROLE_MY_CLIENT")
                // 이 클라이언트로 접근할 수 있는 범위 제한
                // 해당 클라이언트로 API를 접근 했을때 접근 범위를 제한 시키는 속성
                .scopes("member.info.public","member.info.email")
                // 이 클라이언트로 발급된 엑세스토큰의 시간 (단위:초)
                .accessTokenValiditySeconds(60 * 60 * 4)
                // 이 클라이언트로 발급된 리프러시토큰의 시간 (단위:초)
                .refreshTokenValiditySeconds(60 * 60 * 24 * 120)
            .and()
            .withClient("your_client_id")
                .secret("your_client_secret")
                .authorizedGrantTypes("authorization_code", "implicit")
                .authorities("ROLE_YOUR_CLIENT")
                .scopes("member.info.public","member.info.phone")
            .and();
        // @formatter:on
*/
    }
}