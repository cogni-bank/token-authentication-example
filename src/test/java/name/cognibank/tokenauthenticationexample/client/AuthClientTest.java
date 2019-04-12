package name.cognibank.tokenauthenticationexample.client;

import feign.FeignException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthClientTest {

    @Autowired
    private AuthClient authClient;

    @Test(expected = FeignException.class)
    public void checkTokenWithWrongToken() {
        try {
            ResponseEntity response = authClient.checkToken("59ea5304-8f1b-487e-a177-7ecfac7cfefc");
        } catch (FeignException e) {
            assertEquals("Should respond a HTTP 404", HttpStatus.NOT_FOUND.value(),
                    e.status());

            throw e;
        }
    }

    @Test
    public void checkTokenWithCorrectToken() {
        ResponseEntity response = authClient.checkToken("730e279d-1004-4573-bb0c-7e5b8609708d");

        assertEquals("Should respond a HTTP OK", HttpStatus.OK.value(), response.getStatusCode().value());
    }

}