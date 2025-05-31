package com.example.ProjetoAssistenciaJuridica.Config;

import org.springframework.beans.factory.annotation.Autowired; // <-- ADICIONAR SE NÃO EXISTIR
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // <-- ADICIONAR
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler; // <-- ADICIONAR SE NÃO EXISTIR
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired // <-- ADICIONAR ESTA LINHA (injeta o handler criado no Passo 1)
    private AuthenticationSuccessHandler customAuthenticationSuccessHandler;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http ) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // Permite acesso público a estas URLs
                        .requestMatchers("/", "/home", "/entrar", "/login",
                                "/registrar", "/cadastrocliente", "/cadastroadvogado",
                                "/css/**", "/js/**", "/images/**", "/webjars/**" )
                        .permitAll()

                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/entrar")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("senha")
                        //.defaultSuccessUrl("/", true)
                        .successHandler(customAuthenticationSuccessHandler) // <-- ADICIONAR ESTA LINHA (usa o handler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/entrar?logout")
                        .permitAll()
                );
        return http.build( );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

