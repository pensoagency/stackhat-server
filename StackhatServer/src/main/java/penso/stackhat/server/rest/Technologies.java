package penso.stackhat.server.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import penso.stackhat.server.filter.JWTTokenNeeded;
import penso.stackhat.server.model.NewTechnologyRequest;

/**
 * Root resource (exposed at "databases" path)
 */
@Path("/technologies")
public class Technologies {
    /**
     * Method handling HTTP POST requests. The returned object will be sent to the
     * client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response postIt(final NewTechnologyRequest request) {

        // TODO: This endpoint should take the values 
        // supplied in the request parameter and use a method on the 
        // Database class to add a new technology to the DATABASE tab

        // implement here..

        // response
        return Response.ok()
            .build();
    }
}
