package com.woolog.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woolog.config.handler.Http401Handler;
import com.woolog.config.handler.Http403Handler;
import com.woolog.config.handler.LoginFailHandler;
import com.woolog.domain.User;
import com.woolog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Collection;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ObjectMapper objectMapper;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers("/favicon.ico", "/error")
                .requestMatchers(new AntPathRequestMatcher("/h2-console/**")
                );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers("/auth/login" ).permitAll()
                                .requestMatchers("/auth/signup" ).permitAll()
                                .requestMatchers("/user").hasRole("USER")
                                .requestMatchers("/admin").hasRole("ADMIN")
//                                .requestMatchers("/admin")
//                                    .access(new WebExpressionAuthorizationManager(
//                                        "hasRole('ADMIN') AND hasAuthority('WRITE')"
//                                ))
                                .anyRequest().authenticated())
                .formLogin(formLoginConfigurer -> {
                    formLoginConfigurer
                            .loginPage("/auth/login")
                            .loginProcessingUrl("/auth/login")
                            .usernameParameter("username")
                            .passwordParameter("password")
                            .defaultSuccessUrl("/")
                            .failureHandler(new LoginFailHandler(objectMapper));
                })
                .exceptionHandling(e-> {
                    e.accessDeniedHandler(new Http403Handler(objectMapper));
                    e.authenticationEntryPoint(new Http401Handler(objectMapper));
                })
                .rememberMe(rm-> rm
                        .rememberMeParameter("remember")
                        .alwaysRemember(false)
                        .tokenValiditySeconds(2592000)
                )
                .csrf(AbstractHttpConfigurer::disable) //TODO csrf 찾아보기
                .build();

    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username + "을 찾을 수 업습니다"));

            return new UserPrincipal(user);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
            return new SCryptPasswordEncoder(
                    16,
                    8,
                    1,
                    32,
                    64);
    }


}
