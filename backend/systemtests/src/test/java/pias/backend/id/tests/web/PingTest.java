package pias.backend.id.tests.web;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import pias.backend.id.test.main.web.UrlHelper;
import pias.backend.id.test.main.web.model.RequestPojo;
import pias.backend.id.test.main.web.model.ResponsePojo;
import pias.backend.id.test.main.web.model.WebClient;

public class PingTest {
  @RegisterExtension public WebInstance webInstance = new WebInstance();

  @Test
  public void pingTest() throws Exception {
    final WebClient instance = webInstance.webClient();
    final UrlHelper base = instance.getUrlHelper();
    final UrlHelper url =
        new UrlHelper(
            base.fragment(),
            base.query(),
            "/ping",
            base.port(),
            base.host(),
            base.scheme(),
            base.userInfo());
    final RequestPojo requestPojo = new RequestPojo(Optional.empty(), url.getUrl(), "GET");
    final ResponsePojo responsePojo = instance.makeRequest(requestPojo);
    Assertions.assertEquals(responsePojo, new ResponsePojo(Optional.of("pong"), 200));
  }
}
