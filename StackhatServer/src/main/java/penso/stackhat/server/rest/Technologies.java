package penso.stackhat.server.rest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import penso.stackhat.builtwith.*;
import penso.stackhat.builtwith.models.*;
import penso.stackhat.server.filter.JWTTokenNeeded;
import penso.stackhat.server.rest.DatabaseRequest;

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
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response postIt(final String category) {

        return Response.ok()
            .build();
    }
}
