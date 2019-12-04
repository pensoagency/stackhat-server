package penso.stackhat.server.rest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.Response.ResponseBuilder;

import penso.stackhat.builtwith.*;
import penso.stackhat.server.filter.JWTTokenNeeded;
import penso.stackhat.server.model.NewAuditRequest;

/**
 * Root resource (exposed at "databases" path)
 */
@Path("/audits")
public class Audits {
    // /**
    // * Method handling HTTP GET requests. The returned object will be sent to the
    // * client as "text/plain" media type.
    // *
    // * @return String that will be returned as a text/plain response.
    // */
    // @GET
    // @Produces(MediaType.TEXT_PLAIN)
    // @JWTTokenNeeded
    // public String getIt() {
    // return "Hello World!";
    // }

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
    public Response postIt(final NewAuditRequest request) {

        String pathTemplate = Program.pathTemplate;
        String pathDatabase = Program.pathDatabase;
        String pathAuditsBase = Program.pathAuditsBase;

        String APIKey = Program.APIKey; // API

        // generate uid for this request
        String uid = UUID.randomUUID().toString();

        try {
            String pathDestination = pathAuditsBase + uid + ".xlsx";
            File fileDestination = new File(pathDestination);

            // create a copy of the template
            Files.copy((new File(pathTemplate)).toPath(), fileDestination.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);

            Program program = new Program();
            program.start(request.getUrls(), pathDestination, pathDatabase, APIKey);

            return Response.ok((Object)fileDestination)
                    .header("content-disposition", "attachment; filename = audit.xlsx")
                    .build();

            // // return file
            // StreamingOutput fileStream = new StreamingOutput()) {
            // @Override
            // public void write(java.io.OutputStream output) throws IOException,
            // WebApplicationException {
            // try {
            // String path = pathDestination;

            // java.nio.file.Path filePath = Paths.get(path);
            // byte[] data = Files.readAllBytes(filePath);
            // output.write(data);
            // output.flush();
            // } catch (Exception e) {
            // throw new WebApplicationException("File Not Found !!");
            // }
            // }
            // };

            // return Response.ok(fileStream, MediaType.APPLICATION_OCTET_STREAM)
            // .header("content-disposition", "attachment; filename = file.xlsx").build();

        } catch (Exception ex) {
            return Response.serverError().build();
        }
    }
}
