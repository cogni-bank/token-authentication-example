package name.cognibank.tokenauthenticationexample.config;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * A filter for authenticating tokens coming in Authorization header.
 */
public class AuthenticationFilter extends GenericFilterBean {
    final private AuthenticationManager authenticationManager;

    AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // skip to another filter
        if (!(request instanceof HttpServletRequest)
                || !(response instanceof HttpServletResponse)) {
            chain.doFilter(request, response);
            return;
        }

        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse res = (HttpServletResponse) response;

        final Optional<String> token = Optional.ofNullable(
                req.getHeader("Authorization"));

        // skip to another filter, there isn't any Authorization header.
        if (!token.isPresent()) {
            chain.doFilter(request, response);
            return;
        }

        try {
            processTokenAuthentication(token);

        } catch (AuthenticationException e) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }

        chain.doFilter(request, response);
    }

    /**
     * Processes the token and sets authentication result object to spring security context.
     * @param token A token that we will try to authenticate.
     */
    private void processTokenAuthentication(Optional<String> token) {
        Authentication resultOfAuthentication = tryToAuthenticateWithToken(token);
        SecurityContextHolder.getContext().setAuthentication(resultOfAuthentication);
    }

    /**
     * Creates a authentication request object which will be processed in {@link TokenAuthenticationProvider}
     * @param token A token that we will try to authenticate
     * @return result authentication object
     */
    private Authentication tryToAuthenticateWithToken(Optional<String> token) {
        PreAuthenticatedAuthenticationToken requestAuthentication = new PreAuthenticatedAuthenticationToken(null, token);
        return tryToAuthenticate(requestAuthentication);
    }

    /**
     * Calls {@link TokenAuthenticationProvider}
     * @param requestAuthentication an authentication request object which includes the token.
     * @return result authentication object
     */
    private Authentication tryToAuthenticate(Authentication requestAuthentication) {
        Authentication responseAuthentication = authenticationManager.authenticate(requestAuthentication);
        if (responseAuthentication == null || !responseAuthentication.isAuthenticated()) {
            throw new InternalAuthenticationServiceException("Unable to authenticate Domain User for provided credentials");
        }

        logger.debug("User successfully authenticated");
        return responseAuthentication;
    }
}
