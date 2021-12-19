package pias.backend.id.test.main.web.model;

import lombok.extern.log4j.Log4j2;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.RequestBuilder;
import pias.backend.id.server.PrivacyServer;
import pias.backend.id.test.main.web.UrlHelper;

import java.net.MalformedURLException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Log4j2
public class WebClient {
  private final UrlHelper urlHelper;
  private final AsyncHttpClient asyncHttpClient;

  private WebClient(PrivacyServer privacyServer) {

    this.asyncHttpClient = new DefaultAsyncHttpClient();
    final UrlHelper urlHelper =
        UrlHelper.builder()
            .host(Optional.of(privacyServer.getHostname()))
            .port(Optional.of(privacyServer.getPort()))
            .scheme(Optional.of("http"))
            .build();
    log.info("Setting up test url helper {}", urlHelper);
    this.urlHelper = urlHelper;
  }

  public static WebClient createInstance(final PrivacyServer privacyServer) {
    return new WebClient(privacyServer);
  }

  public ResponsePojo makeRequest(final RequestPojo requestPojo)
      throws ExecutionException, InterruptedException, MalformedURLException {
    final RequestBuilder requestBuilder =
        new RequestBuilder()
            .setMethod(requestPojo.getMethod())
            .setUrl(requestPojo.getUri().toASCIIString());

    return asyncHttpClient
        .prepareRequest(
            requestPojo.getBody().map(requestBuilder::setBody).orElse(requestBuilder).build())
        .execute()
        .toCompletableFuture()
        .thenApplyAsync(
            response ->
                ResponsePojo.builder()
                    .body(
                        response.hasResponseBody()
                            ? Optional.of(response.getResponseBody())
                            : Optional.empty())
                    .code(response.getStatusCode())
                    .build())
        .get();
  }

  public UrlHelper getUrlHelper() {
    return urlHelper;
  }
}
