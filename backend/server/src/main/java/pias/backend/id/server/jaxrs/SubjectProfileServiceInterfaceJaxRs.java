package pias.backend.id.server.jaxrs;

import javax.ws.rs.*;
import pias.backend.id.server.jaxrs.model.SubjectProfileCreateJaxRs;
import pias.backend.id.server.jaxrs.model.SubjectProfileJaxRs;
import pias.backend.id.server.jaxrs.model.SubjectProfileUpdateJaxRs;

@Path("subject-profile")
public interface SubjectProfileServiceInterfaceJaxRs {

  @POST
  @Consumes("application/json")
  @Produces("application/json")
  SubjectProfileJaxRs create(SubjectProfileCreateJaxRs customerProfileCreateJaxRs);

  @PUT
  @Consumes("application/json")
  @Produces("application/json")
  SubjectProfileJaxRs update(SubjectProfileUpdateJaxRs customerProfileUpdateJaxRs);

  @GET
  @Path("{id}")
  @Consumes("application/json")
  @Produces("application/json")
  SubjectProfileJaxRs read(@PathParam("id") String id);

  @GET
  @Path("{id}/{version}")
  @Consumes("application/json")
  @Produces("application/json")
  SubjectProfileJaxRs read(@PathParam("id") String id, @PathParam("version") String version);
}
