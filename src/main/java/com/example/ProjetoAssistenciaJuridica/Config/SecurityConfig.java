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

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private AuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Páginas públicas
                        .requestMatchers(
                                "/", "/index", "/home",
                                "/entrar", "/login",                 // página e endpoint de login
                                "/registrar",                        // abertura de caso (pode deixar auth se quiser)
                                "/cliente/cadastro", "/cliente/cadastrar", "/cadastrocliente", "/cliente/cadastrocliente",
                                "/advogado/cadastrar", "/cadastroadvogado",
                                "/termos", "/privacidade"
                        ).permitAll()
                        // Recursos estáticos
                        .requestMatchers(
                                "/css/**", "/js/**", "/img/**", "/svg/**", "/webjars/**"
                        ).permitAll()
                        // Demais rotas precisam de autenticação
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

        // Se for usar H2-console algum dia:
        // http.headers(headers -> headers.frameOptions(frame -> frame.disable()))
        //   .csrf(csrf -> csrf.ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
}
