package penso.stackhat.server.rest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import javax.json.Json;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import penso.stackhat.builtwith.*;
import penso.stackhat.server.filter.JWTTokenNeeded;
import penso.stackhat.server.model.AuditResponse;
import penso.stackhat.server.model.NewAuditRequest;
import penso.stackhat.server.util.ParamRunnable;

/**
 * Root resource (exposed at "audits" path)
 */
@Path("/audits")
public class Audits {

    // @GET
    // @Produces(MediaType.APPLICATION_JSON)
    // @JWTTokenNeeded
    // public Response getIt(String id) {

    // try {
    // List<String> lines = Files.readAllLines(Paths.get(Program.pathAuditsBase + id
    // + ".json"));

    // // return
    // return Response.ok(lines.toString()).build();

    // } catch (FileNotFoundException e) {
    // return Response.status(404).build();
    // } catch (Exception e) {
    // return Response.status(500).build();
    // }

    // }

    @GET
    @JWTTokenNeeded
    @Path("{id}/download")
    public Response getIt(@PathParam("id") final String id) {

        StreamingOutput fileStream = new StreamingOutput() {
            @Override
            public void write(java.io.OutputStream output) throws IOException, WebApplicationException {
                try {
                    java.nio.file.Path path = Paths.get(Program.pathAuditsBase + id + ".xlsx");
                    byte[] data = Files.readAllBytes(path);
                    output.write(data);
                    output.flush();
                } catch (Exception e) {
                    throw new WebApplicationException("File Not Found !!");
                }
            }
        };

        String fileName = String.format("audit-{0}.xlsx", id);

        return Response.ok(fileStream, MediaType.APPLICATION_OCTET_STREAM)
                .header("content-disposition", String.format("attachment; filename = {0}", fileName))
                .header("x-filename", fileName)
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @JWTTokenNeeded
    public Response getAll() {

        try {
            ArrayList<Object> result = new ArrayList<Object>();

            File dir = new File(Program.pathAuditsBase);
            File[] files = dir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".json");
                }
            });

            JSONParser parser = new JSONParser();
            for (File jsonFile : files) {
                result.add(parser.parse(new FileReader(jsonFile.getAbsolutePath())));
            }

            // return
            return Response.ok(result).build();

        } catch (FileNotFoundException e) {
            return Response.status(404).build();
        } catch (Exception e) {
            return Response.status(500).build();
        }

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
    @JWTTokenNeeded
    public Response postIt(final NewAuditRequest request) {

        // max 100 domains
        if (request.urls.length > 100) {
            Response.ResponseBuilder tooManyUrls = Response.status(Status.BAD_REQUEST);
            tooManyUrls.entity("Maximum 100 domains per audit.");
            return tooManyUrls.build();
        }

        // build log / result
        AuditResponse result = new AuditResponse(UUID.randomUUID().toString(), request.urls, request.name, false,
                false);

        try {

            // write initial detail file
            writeAuditJson(result);

            new Thread(new ParamRunnable<AuditResponse>(result) {

                @Override
                public void run() {
                    try {
                        String pathTemplate = Program.pathTemplate;
                        String pathDatabase = Program.pathDatabase;
                        String pathAuditsBase = Program.pathAuditsBase;
                        String APIKey = Program.APIKey; // API

                        String pathDestination = pathAuditsBase + this.parameter.id + ".xlsx";
                        File fileDestination = new File(pathDestination);

                        // create a copy of the template
                        Files.copy((new File(pathTemplate)).toPath(), fileDestination.toPath(),
                                StandardCopyOption.REPLACE_EXISTING);

                        Program program = new Program();
                        program.start(this.parameter.urls, this.parameter.log, pathDestination, pathDatabase, APIKey);

                        // write success
                        this.parameter.setIsReady(true);
                        writeAuditJson(this.parameter);
                    } catch (Exception ex) {
                        // write failed
                        this.parameter.setIsError(true);
                        writeAuditJson(this.parameter);
                    }
                }
            }).start();

            return Response.ok(result).build();

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
        json.put("created", result.created);
        json.put("log", result.log);

        try {
            try (FileWriter fileResultWriter = new FileWriter(Program.pathAuditsBase + result.id + ".json", false)) {
                fileResultWriter.write(json.toString());
                fileResultWriter.close();
            }
        } catch (Exception ex) {
        }
    }
}
