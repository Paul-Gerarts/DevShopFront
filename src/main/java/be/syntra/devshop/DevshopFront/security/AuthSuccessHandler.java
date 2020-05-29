package be.syntra.devshop.DevshopFront.security;

import be.syntra.devshop.DevshopFront.exceptions.UserRoleNotFoundException;
import be.syntra.devshop.DevshopFront.models.DataStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;

@Slf4j
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private DataStore dataStore;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        handle(request, response, authentication);
        clearAuthenticationAttributes(request);
    }

    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        String targetUrl = determineTargetUrl(authentication);

        redirectStrategy.sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
                return "/admin/overview";
            } else if (grantedAuthority.getAuthority().equals("ROLE_USER")) {
                if (checkDataStoreForRedirectStrategyAfterLogin()) {
                    disableDataStoreRedirectStrategyAfterLogin();
                    return "/users/cart";
                }
                return "/products";
            } else {
                throw new UserRoleNotFoundException("Please contact your admin");
            }
        }
        return "auth/login";
    }

    private void disableDataStoreRedirectStrategyAfterLogin() {
        dataStore.getMap().put("redirectToCartAfterUserSuccessfulLogin", false);
    }

    private boolean checkDataStoreForRedirectStrategyAfterLogin() {
        return dataStore.getMap().get("redirectToCartAfterUserSuccessfulLogin");
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}
