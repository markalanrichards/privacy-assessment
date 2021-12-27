package pias.backend.id.server.servlets;

import java.io.IOException;
import javax.servlet.Servlet;
import org.apache.avro.Protocol;
import org.apache.avro.ipc.ResponderServlet;
import org.apache.avro.ipc.specific.SpecificResponder;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.impl.factory.Maps;
import org.eclipse.collections.impl.tuple.Tuples;
import pias.backend.PIAAvpr;
import pias.backend.avro.CustomerProfileAvpr;
import pias.backend.avro.SubjectProfileAvpr;
import pias.backend.id.server.avro.CustomerProfileAvprImpl;
import pias.backend.id.server.avro.PIAAvprImpl;
import pias.backend.id.server.avro.SubjectProfileAvprImpl;
import pias.backend.id.server.database.CustomerProfileService;
import pias.backend.id.server.database.SubjectProfileService;

public class AvroServlets {
  public ImmutableMap<String, Servlet> avroServlets(
      CustomerProfileService mysqlCustomerProfileService,
      SubjectProfileService mysqlSubjectProfileService,
      PIAAvprImpl impl) {
    try {
      return unsafeGetService(mysqlCustomerProfileService, mysqlSubjectProfileService, impl);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private ImmutableMap<String, Servlet> unsafeGetService(
      CustomerProfileService mysqlCustomerProfileService,
      SubjectProfileService mysqlSubjectProfileService,
      PIAAvprImpl impl)
      throws IOException {

    return Maps.mutable
        .<Protocol, Servlet>empty()
        .withKeyValue(
            CustomerProfileAvpr.PROTOCOL,
            getResponderServlet(
                new CustomerProfileAvprImpl(mysqlCustomerProfileService),
                CustomerProfileAvpr.class))
        .withKeyValue(
            SubjectProfileAvpr.PROTOCOL,
            getResponderServlet(
                new SubjectProfileAvprImpl(mysqlSubjectProfileService), SubjectProfileAvpr.class))
        .withKeyValue(PIAAvpr.PROTOCOL, getResponderServlet(impl, PIAAvpr.class))
        .collect(
            (protocol, servlet) ->
                Tuples.pair(
                    String.format("%s.%s", protocol.getNamespace(), protocol.getName()), servlet))
        .toImmutable();
  }

  private <T> ResponderServlet getResponderServlet(T impl, Class<? extends T> clazz)
      throws IOException {
    SpecificResponder responder = new SpecificResponder(clazz, impl);
    return new ResponderServlet(responder);
  }
}
