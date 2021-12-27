package pias.backend.id.server.jaxrs;

import javax.ws.rs.*;
import pias.backend.id.server.jaxrs.model.CustomerProfileCreateJaxRs;
import pias.backend.id.server.jaxrs.model.CustomerProfileJaxRs;
import pias.backend.id.server.jaxrs.model.CustomerProfileUpdateJaxRs;

@Path("customer-profile")
public interface CustomerProfileServiceInterfaceJaxRs {

  @POST
  @Consumes("application/json")
  @Produces("application/json")
  CustomerProfileJaxRs create(CustomerProfileCreateJaxRs customerProfileCreateJaxRs);

  @PUT
  @Consumes("application/json")
  @Produces("application/json")
  CustomerProfileJaxRs update(CustomerProfileUpdateJaxRs customerProfileUpdateJaxRs);

  @GET
  @Path("{id}")
  @Consumes("application/json")
  @Produces("application/json")
  CustomerProfileJaxRs read(@PathParam("id") String id);

  @GET
  @Path("{id}/{version}")
  @Consumes("application/json")
  @Produces("application/json")
  CustomerProfileJaxRs read(@PathParam("id") String id, @PathParam("version") String version);
}
