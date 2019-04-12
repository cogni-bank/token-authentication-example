package name.cognibank.tokenauthenticationexample.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Just an echo rest api to test.
 */
@RestController
public class MyController {

    @GetMapping(path = "/echo/{message}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public String echo(@PathVariable String message) {
        return message;
    }

}
