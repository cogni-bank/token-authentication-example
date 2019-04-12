package name.cognibank.tokenauthenticationexample.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private TokenAuthenticationProvider tokenAuthenticationProvider;

    /**
     * Configures Spring-Security for authenticating tokens coming from the UI app.
     * @param http standard spring security helper for http request.
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // disable sessions
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // disable CSRF
                .and().csrf().disable()

                // authenticate every requests
                .authorizeRequests()
                .anyRequest()
                .authenticated()

                .and().httpBasic();

        http.addFilterBefore(new AuthenticationFilter(authenticationManager()), BasicAuthenticationFilter.class);
    }

    /**
     * Configures AuthenticationManager with {@link TokenAuthenticationProvider}. This provider will be used in {@link AuthenticationFilter}
     * @param auth standard spring security helper for http request.
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(tokenAuthenticationProvider);
    }

}