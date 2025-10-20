package com.example.ProjetoAssistenciaJuridica.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration @EnableMethodSecurity
public class SecurityConfig {

    @Autowired private AuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // públicas
                        .requestMatchers(
                                "/", "/index", "/home",
                                "/entrar", "/login",
                                "/registrar/advogado",
                                "/cliente/cadastro", "/cliente/cadastrocliente", "/registrar/cliente", "/cadastrocliente",

                                "/cadastrocliente", "/cliente/cadastrar",
                                "/advogado/cadastrar", "/cadastroadvogado",
                                "/termos", "/privacidade",
                                "/css/**", "/js/**", "/img/**", "/svg/**", "/webjars/**"
                        ).permitAll()
                        // estáticos
                        .requestMatchers("/css/**", "/js/**", "/img/**", "/svg/**", "/webjars/**", "/favicon.ico").permitAll()
                        // TODO: estas rotas do header ainda não existem? deixe-as públicas para não redirecionar pro login
                        .requestMatchers("/como-funciona", "/quem-atendemos", "/areas", "/voluntarios**").permitAll()
                        .requestMatchers("/cliente/solicitacao/nova").hasRole("CLIENTE")
                        // tudo mais precisa de login (ex.: /registrar)
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/entrar")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("senha")
                        .successHandler(customAuthenticationSuccessHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/entrar?logout")
                        .permitAll()
                );

        return http.build();
    }

    @Bean public PasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder(); }
}