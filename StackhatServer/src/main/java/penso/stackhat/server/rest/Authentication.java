package penso.stackhat.server.rest;

import java.security.Key;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import penso.stackhat.server.util.KeyGenerator;

@Path("/token")
public class Authentication {

    @Context
    private UriInfo uriInfo;

    @Inject
    private KeyGenerator keyGenerator;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response authenticateUser(@FormParam("username") String username, 
                                     @FormParam("password") String password) {

        try {

            // Authenticate the user using the credentials provided
            authenticate(username, password);

            // Issue a token for the user
            String token = issueToken(username);

            // Return the token on the response
            return Response.ok(token).build();

        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }      
    }

    private void authenticate(String username, String password) throws Exception {
        // Authenticate against a database, LDAP, file or whatever
        // Throw an Exception if the credentials are invalid
        if (!username.equals("penso") || !password.equals("p3n5oagrader!")) {
            throw new Exception("Invalid username and or password");
        }

    }

    private String issueToken(String username) {
        // Issue a token (can be a random String persisted to a database or a JWT token)
        // The issued token must be associated to a user
        // Return the issued token

        Key key = keyGenerator.generateKey();
        String jwtToken = Jwts.builder()
                .setSubject(username)
                .setIssuer(uriInfo.getAbsolutePath().toString())
                .setIssuedAt(new Date())
                .setExpiration(toDate(LocalDateTime.now().plusMinutes(15L)))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        return jwtToken;        
    }

    // ======================================
    // =          Private methods           =
    // ======================================

    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }    
}