package penso.stackhat.server;

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
import penso.stackhat.server.DatabaseRequest;;

/**
 * Root resource (exposed at "databases" path)
 */
@Path("categories")
public class Categories {
    /**
     * Method handling HTTP GET requests. The returned object will be sent to the
     * client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<Category> getIt() 
        throws IOException {

		Database db = new Database();
		ArrayList<Category> categories;
		categories=db.getAllCategoriesDetail(Program.pathDatabase);

        return categories;
    }

    /**
     * Method handling HTTP POST requests. The returned object will be sent to the
     * client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postIt(final String category) {

        return Response.ok()
            .build();
    }
}
