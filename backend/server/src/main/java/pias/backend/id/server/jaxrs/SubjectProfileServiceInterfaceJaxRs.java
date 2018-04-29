package pias.backend.id.server.jaxrs;

import pias.backend.id.server.jaxrs.model.SubjectProfileCreateJaxRs;
import pias.backend.id.server.jaxrs.model.SubjectProfileJaxRs;
import pias.backend.id.server.jaxrs.model.SubjectProfileUpdateJaxRs;
import pias.backend.id.server.jaxrs.model.SubjectProfileJaxRs;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("subject-profile")
public interface SubjectProfileServiceInterfaceJaxRs {

    @POST
    @Consumes( "application/json" )
    @Produces( "application/json" )
    SubjectProfileJaxRs create(SubjectProfileCreateJaxRs customerProfileCreateJaxRs);

    @PUT
    @Consumes( "application/json" )
    @Produces( "application/json" )
    SubjectProfileJaxRs update(SubjectProfileUpdateJaxRs customerProfileUpdateJaxRs);

    @GET
    @Path("{id}")
    @Consumes( "application/json" )
    @Produces( "application/json" )
    SubjectProfileJaxRs read(@PathParam("id") String id);

    @GET
    @Path("{id}/{version}")
    @Consumes( "application/json" )
    @Produces( "application/json" )
    SubjectProfileJaxRs read(@PathParam("id") String id, @PathParam("version") String version);



}
