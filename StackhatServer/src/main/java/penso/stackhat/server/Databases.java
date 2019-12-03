package penso.stackhat.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
import penso.stackhat.server.DatabaseRequest;;

/**
 * Root resource (exposed at "databases" path)
 */
@Path("databases")
public class Databases {
    /**
     * Method handling HTTP GET requests. The returned object will be sent to the
     * client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Hello World!";
    }

    /**
     * Method handling HTTP POST requests. The returned object will be sent to the
     * client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postIt(final DatabaseRequest request) {

        String path = "./tmp.xlsx";
        String pathDatabase = Program.pathDatabase;

        String APIKey = Program.APIKey; // API

        try {
            Program program = new Program();
            program.start(request.getUrls(), path, pathDatabase, APIKey);

            // return file
            StreamingOutput fileStream = new StreamingOutput() {
                @Override
                public void write(java.io.OutputStream output) throws IOException, WebApplicationException {
                    try {
                        String path = "./tmp.xlsx";

                        java.nio.file.Path filePath = Paths.get(path);
                        byte[] data = Files.readAllBytes(filePath);
                        output.write(data);
                        output.flush();
                    } catch (Exception e) {
                        throw new WebApplicationException("File Not Found !!");
                    }
                }
            };

            return Response.ok(fileStream, MediaType.APPLICATION_OCTET_STREAM)
                    .header("content-disposition", "attachment; filename = file.xlsx").build();

        } catch (Exception ex) {
            return Response.serverError().build();
        }
    }
}
