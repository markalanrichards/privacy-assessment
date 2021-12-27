package pias.backend.id.test.main.web.model;

import java.net.MalformedURLException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.RequestBuilder;
import pias.backend.id.server.PrivacyServer;
import pias.backend.id.test.main.web.UrlHelper;

public class WebClient {
  private static final Logger LOGGER = LogManager.getLogger(WebClient.class);
  private final UrlHelper urlHelper;
  private final AsyncHttpClient asyncHttpClient;

  private WebClient(PrivacyServer privacyServer) {

    this.asyncHttpClient = new DefaultAsyncHttpClient();
    final String hostname = privacyServer.getHostname();
    final int port = privacyServer.getPort();
    final String scheme = "http";

    final UrlHelper urlHelper = new UrlHelper(null, null, null, port, hostname, scheme, null);
    LOGGER.info("Setting up test url helper {}", urlHelper);
    this.urlHelper = urlHelper;
  }

  public static WebClient createInstance(final PrivacyServer privacyServer) {
    return new WebClient(privacyServer);
  }

  public ResponsePojo makeRequest(final RequestPojo requestPojo)
      throws ExecutionException, InterruptedException, MalformedURLException {
    final RequestBuilder requestBuilder =
        new RequestBuilder()
            .setMethod(requestPojo.method())
            .setUrl(requestPojo.uri().toASCIIString());

    return asyncHttpClient
        .prepareRequest(
            requestPojo.body().map(requestBuilder::setBody).orElse(requestBuilder).build())
        .execute()
        .toCompletableFuture()
        .thenApplyAsync(
            response ->
                new ResponsePojo(
                    response.hasResponseBody()
                        ? Optional.of(response.getResponseBody())
                        : Optional.empty(),
                    response.getStatusCode()))
        .get();
  }

  public UrlHelper getUrlHelper() {
    return urlHelper;
  }
}
