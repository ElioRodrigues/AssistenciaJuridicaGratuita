package com.example.ProjetoAssistenciaJuridica.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.http.HttpMethod; // Import não utilizado removido
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer; // Import não utilizado removido
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint; // Import não utilizado removido
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

// import static org.springframework.security.config.Customizer.withDefaults; // Import não utilizado removido

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
                        // ATENÇÃO: Alterei para .anyRequest().authenticated() conforme a lógica original. Se você realmente quer tudo público, mantenha .permitAll()
                        .anyRequest().authenticated() // <-- Verifique se é isso mesmo que deseja
                )
                .formLogin(form -> form
                        .loginPage("/entrar")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("senha")
                        .defaultSuccessUrl("/", true) // Redireciona para home após sucesso
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

