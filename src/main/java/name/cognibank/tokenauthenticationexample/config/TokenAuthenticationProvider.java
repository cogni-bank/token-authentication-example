package name.cognibank.tokenauthenticationexample.config;

import feign.FeignException;
import name.cognibank.tokenauthenticationexample.client.AuthClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * This authentication provider calls the authentication micro service and verifies token validity.
 */
@Component
public class TokenAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private AuthClient authClient; // Feign client

    /**
     * Reads the authentication object and expects a token in credentials.
     * @param authentication should be a {@link PreAuthenticatedAuthenticationToken} object.
     * @return authentication result
     * @throws AuthenticationException is thrown when there isn't a token, the token is unauthorized or the token cannot be validated.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Optional<String> token = (Optional) authentication.getCredentials();

        if (!token.isPresent() || token.get().isEmpty()) {
            throw new BadCredentialsException("Invalid token");
        }

        try {
            // this service returns a http ok status when the token is valid.
            // and returns http not found status when the token is invalid.
            // feign client throws a {@link FeignException} when the response has anything other than 200 status.
            authClient.checkToken(token.get()); // todo: receive userId and roles while checking auth token
            // todo: set userId to principles
            //authentication = new PreAuthenticatedAuthenticationToken(userId, token, roles);
            authentication.setAuthenticated(true); //
            return authentication;
        } catch (FeignException e) {
            throw new BadCredentialsException("Cannot verify the token", e);
        }
    }

    /**
     * We expect a {@link PreAuthenticatedAuthenticationToken} object to validate
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return PreAuthenticatedAuthenticationToken.class.equals(authentication);
    }

}
