package pias.backend.id.server.jaxrs;

import pias.backend.id.server.entity.CustomerProfile;
import pias.backend.id.server.jaxrs.model.CustomerProfileCreateJaxRs;
import pias.backend.id.server.jaxrs.model.CustomerProfileJaxRs;
import pias.backend.id.server.jaxrs.model.CustomerProfileUpdateJaxRs;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("customer-profile")
public interface CustomerProfileServiceInterfaceJaxRs {

    @POST
    @Consumes( "application/json" )
    @Produces( "application/json" )
    CustomerProfileJaxRs create(CustomerProfileCreateJaxRs customerProfileCreateJaxRs);

    @PUT
    @Consumes( "application/json" )
    @Produces( "application/json" )
    CustomerProfileJaxRs update(CustomerProfileUpdateJaxRs customerProfileUpdateJaxRs);

    @GET
    @Path("{id}")
    @Consumes( "application/json" )
    @Produces( "application/json" )
    CustomerProfileJaxRs read(@PathParam("id") String id);

    @GET
    @Path("{id}/{version}")
    @Consumes( "application/json" )
    @Produces( "application/json" )
    CustomerProfileJaxRs read(@PathParam("id") String id, @PathParam("version") String version);



}
