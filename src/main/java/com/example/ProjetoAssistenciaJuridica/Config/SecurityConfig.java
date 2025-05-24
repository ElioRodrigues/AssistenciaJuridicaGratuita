package com.example.ProjetoAssistenciaJuridica.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http ) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // Permite acesso público a estas URLs
                        .requestMatchers("/", "/home", "/entrar", "/login",
                                "/registrar", "/cadastrocliente", "/cadastroadvogado",
                                "/css/**", "/js/**", "/images/**", "/webjars/**" ) // Adicione outros recursos estáticos se necessário
                        .permitAll()
                        // Qualquer outra requisição precisa de autenticação
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/entrar")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email") // Mantém a correção anterior
                        .passwordParameter("senha") // Mantém a correção anterior
                        .defaultSuccessUrl("/", true) // Ou redirecionar para um painel específico?
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