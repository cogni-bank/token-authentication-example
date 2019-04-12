package name.cognibank.tokenauthenticationexample;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

import javax.servlet.Filter;

@SpringBootApplication
@EnableFeignClients
public class TokenAuthenticationExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(TokenAuthenticationExampleApplication.class, args);
    }

}
