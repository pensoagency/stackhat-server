package penso.stackhat.server.rest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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

import org.json.JSONObject;

import penso.stackhat.builtwith.*;
import penso.stackhat.server.filter.JWTTokenNeeded;
import penso.stackhat.server.model.AuditResponse;
import penso.stackhat.server.model.NewAuditRequest;
import penso.stackhat.server.util.ParamRunnable;

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

        // build log / result
        AuditResponse result = new AuditResponse(UUID.randomUUID().toString(), request.urls, request.name, false, false);

        try {

            // write initial detail file
            writeAuditJson(result);    

            new Thread(new ParamRunnable<AuditResponse>(result) {

                @Override
                public void run() {
                    String pathTemplate = Program.pathTemplate;
                    String pathDatabase = Program.pathDatabase;
                    String pathAuditsBase = Program.pathAuditsBase;
                    String APIKey = Program.APIKey; // API

                    String pathDestination = pathAuditsBase + this.parameter.id + ".xlsx";
                    File fileDestination = new File(pathDestination);

                    try {
                        // create a copy of the template
                        Files.copy((new File(pathTemplate)).toPath(), fileDestination.toPath(),
                                StandardCopyOption.REPLACE_EXISTING);

                        Program program = new Program();
                        program.start(request.getUrls(), pathDestination, pathDatabase, APIKey);

                        // write success
                        this.parameter.setIsReady(true);
                        writeAuditJson(this.parameter);                        
                    } 
                    catch (Exception ex) 
                    {
                        // write failed
                        this.parameter.setIsError(true);
                        writeAuditJson(this.parameter);                        
                    }
                }
            });

            return Response.ok(result).build();

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

    private void writeAuditJson(AuditResponse result) {
        // create json version
        JSONObject json = new JSONObject();
        json.put("id", result.id);
        json.put("urls", result.urls);
        json.put("title", result.title);
        json.put("isReady", result.isReady);
        json.put("isError", result.isError);

        try {
            try (FileWriter fileResultWriter = new FileWriter(Program.pathAuditsBase + result.id + ".json")) {
                fileResultWriter.write(json.toString());
            }
        } catch (Exception ex) {
        }
    }    
}
