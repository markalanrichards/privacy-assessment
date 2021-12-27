package pias.backend.id.test.main.web.model;

import java.net.URI;
import java.util.Optional;

public record RequestPojo(Optional<String> body, URI uri, String method) {}
