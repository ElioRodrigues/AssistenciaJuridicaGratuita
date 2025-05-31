package com.example.ProjetoAssistenciaJuridica.Config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component // Registra como um Bean gerenciado pelo Spring
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy( );

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String targetUrl = determineTargetUrl(authentication);

        if (response.isCommitted()) {
            System.out.println("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        redirectStrategy.sendRedirect(request, response, targetUrl);
        clearAuthenticationAttributes(request); // Opcional: Limpa atributos da sessão
    }

    protected String determineTargetUrl(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority grantedAuthority : authorities) {
            // Verifica as roles e define a URL de destino
            if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
                return "/admin/dashboard"; // Redireciona Admin
            } else if (grantedAuthority.getAuthority().equals("ROLE_ADVOGADO")) {
                return "/advogado/dashboard"; // Redireciona Advogado
            } else if (grantedAuthority.getAuthority().equals("ROLE_CLIENTE")) {
                return "/cliente/dashboard"; // Redireciona Cliente
            }
        }
        // Fallback caso não encontre role conhecida
        return "/home";
    }

    // Opcional: Limpa atributos da sessão
    protected void clearAuthenticationAttributes(HttpServletRequest request) {
        jakarta.servlet.http.HttpSession session = request.getSession(false );
        if (session == null) {
            return;
        }
        session.removeAttribute(org.springframework.security.web.WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}
