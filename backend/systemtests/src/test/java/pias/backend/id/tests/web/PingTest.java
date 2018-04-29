package pias.backend.id.tests.web;

import pias.backend.id.test.main.web.model.RequestPojo;
import pias.backend.id.test.main.web.model.ResponsePojo;
import pias.backend.id.test.main.web.model.WebClient;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.util.Optional;

public class PingTest {
    @Rule
    public WebInstance webInstance = new WebInstance();

    @Test
    public void pingTest() throws Exception {
        final WebClient instance = webInstance.webClient();

        final ResponsePojo responsePojo = instance.makeRequest(RequestPojo.builder()
                .method("GET")
                .uri(instance.getUrlHelper()
                        .toBuilder()
                        .path(Optional.of("/ping"))
                        .build()
                        .getUrl())
                .build());
        Assert.assertEquals(responsePojo, ResponsePojo.builder().code(200).body(Optional.of("pong")).build());


    }
}
