package pias.backend.id.server.jaxrs;

import pias.backend.id.server.jaxrs.model.PIACreateJaxRs;
import pias.backend.id.server.jaxrs.model.PIAJaxRs;
import pias.backend.id.server.jaxrs.model.PIAUpdateJaxRs;

import javax.ws.rs.*;

@Path("pia-annex-one")
public interface PIAServiceInterfaceJaxRs {

  @POST
  @Consumes("application/json")
  @Produces("application/json")
  PIAJaxRs create(PIACreateJaxRs customerProfileCreateJaxRs);

  @PUT
  @Consumes("application/json")
  @Produces("application/json")
  PIAJaxRs update(PIAUpdateJaxRs customerProfileUpdateJaxRs);

  @GET
  @Path("{id}")
  @Consumes("application/json")
  @Produces("application/json")
  PIAJaxRs read(@PathParam("id") String id);

  @GET
  @Path("{id}/{version}")
  @Consumes("application/json")
  @Produces("application/json")
  PIAJaxRs read(@PathParam("id") String id, @PathParam("version") String version);
}
