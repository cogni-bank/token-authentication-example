package name.cognibank.tokenauthenticationexample.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * A feign client depends on feign and ribbon.
 */
@FeignClient("feign-login-client")
public interface AuthClient {

    @RequestMapping(method = RequestMethod.GET, value = "/forward")
    ResponseEntity checkToken(@RequestHeader("Authorization") String token);

}
